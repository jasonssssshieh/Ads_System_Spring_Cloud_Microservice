package com.jason.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.jason.ad.mysql.TemplateHolder;
import com.jason.ad.mysql.dto.BinlogRowData;
import com.jason.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {
    private String dbName;
    private String tableName;

    //String 就是对应一张表, Ilistener就是监听方法
    private Map<String, Ilistener> ilistenerMap = new HashMap<>();


    @Autowired
    private TemplateHolder templateHolder;

    private String genKey(String dbName, String tableName){
        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName,
                          Ilistener ilistener){
        log.info("register : {}-{}", _dbName, _tableName);
        this.ilistenerMap.put(genKey(_dbName,_tableName), ilistener);
    }

    @Override
    public void onEvent(Event event) {
        //event里面就是我们BinLog的全部内容
        // 目标是把这个event解析成BinlogRowData
        // 然后这个BinlogRowData传递给对应的listener, 实现增量数据的更新

        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);

        if(type == EventType.TABLE_MAP){
            //TABLE_MAP里面包含了接下来要操作的数据库和数据表的名字
            TableMapEventData data = event.getData();
            //data里面就包含了所有的操作数据
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        if(type != EventType.EXT_UPDATE_ROWS
                && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS){
            //如果不属于三种之一 那么我们不需要处理
            return;
        }

        // 判断表名和库名是否已经完成填充
        if(StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            log.error("no meta data event");
            return;
        }

        //找出对应表有兴趣的监听器 就是前面那个已经完成注册了的, 从map里获取
        String key = genKey(this.dbName, this.tableName);
        Ilistener ilistener = this.ilistenerMap.get(key);
        if(ilistener == null){
            //代表没有监听器来监听
            log.debug("skip {}", key);
            return;
        }
        log.info("trigger event: {}", type.name());

        try{
            BinlogRowData rowData = buildRowData(event.getData());
            if(rowData == null){
                return;
            }
            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.dbName = "";
            this.tableName = "";
        }
    }

    private List<Serializable[]> getAfterValues(EventData eventData){
        //这个方法是为了我们能够得到在操作之后的data list 但由于我们的删除和add是没有的,那么就默认为空
        if(eventData instanceof WriteRowsEventData){
            //强转,然后得到rows的数据 也就是新添加的数据
            return ((WriteRowsEventData) eventData).getRows();
        }

        if(eventData instanceof UpdateRowsEventData){
            //更新操作其实是一个key value pair, <before-after> 因此这里我们是要得到value部分
            return ((UpdateRowsEventData) eventData).getRows().
                    stream().map(Map.Entry::getValue).
                    collect(Collectors.toList());
        }

        if(eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }

    private BinlogRowData buildRowData(EventData eventData){
        TableTemplate tableTemplate = templateHolder.getTable(tableName);
        if(tableTemplate == null){
            log.warn("table {} not found", tableName);
            return null;
        }
        //将来用于填充binlogrowdata的after Map
        List<Map<String, String>> afterMapList = new ArrayList<>();

        for (Serializable[] after : getAfterValues(eventData)) {
            Map<String, String> afterMap = new HashMap<>();

            int colLen = after.length;

            for(int ix = 0; ix <colLen; ++ix){

                //取出当前位置对应的列名
                String colName = tableTemplate.getPosMap().get(ix);

                //如果没有则说明不关心这个列

                if(colName == null){
                    log.debug("ignore position: {}", ix);
                    continue;
                }
                String colValue = after[ix].toString();
                afterMap.put(colName, colValue);
            }

            afterMapList.add(afterMap);
        }

        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTableTemplate(tableTemplate);
        return rowData;
    }
}
