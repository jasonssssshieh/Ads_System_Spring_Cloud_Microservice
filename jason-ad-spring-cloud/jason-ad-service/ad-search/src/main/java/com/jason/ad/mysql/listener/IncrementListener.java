package com.jason.ad.mysql.listener;


import com.github.shyiko.mysql.binlog.event.EventType;
import com.jason.ad.mysql.constant.Constant;
import com.jason.ad.mysql.constant.OpType;
import com.jason.ad.mysql.dto.BinlogRowData;
import com.jason.ad.mysql.dto.MySqlRowData;
import com.jason.ad.mysql.dto.TableTemplate;
import com.jason.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//增量数据的监听器
@Slf4j
@Component
public class IncrementListener implements Ilistener{

    //根据name来投递 投递的方式很多 可以投递到kafka, 增量接口里面 等等...
    @Resource(name = "indexSender")
    private ISender sender;

    private final AggregationListener aggregationListener;

    @Autowired
    IncrementListener(AggregationListener aggregationListener){
        this.aggregationListener = aggregationListener;
    }


    //实现表的注册 如果不注册 那么就算发生了更新 我们也不会在bin log里面发现他
    //这个注册的行为应该是发生在我们increment listener实例化的时候, 在放入容器的时候 就应该注册

    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db and table info");

        //这里k是我们的table name, v是数据库代的名字
        Constant.table2Db.forEach((k, v) ->
                aggregationListener.register(v, k, this)
        );
        //this就是我们这个对增量数据的监听器
    }

    //onEvent主要目的就是把BinlogRowData 转换成 我们定义的 MySqlRowData, 然后将row data 投递出去
    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTableTemplate();
        EventType eventType = eventData.getEventType();

        //包装成最后需要投递的数据
        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTableTemplate().getLevel());
        OpType opType = OpType.to(eventData.getEventType());
        rowData.setOpType(opType);

        // 取出模板中该操作对于的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if(fieldList == null){
            //不是我们想要处理的类型
            log.warn("{} not support for {}", opType, table.getTableName());
            return;
        }

        for(Map<String, String> afterMap : eventData.getAfter()){
            //发生变化的列以及每一列在变化之后的列值
            Map<String, String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();

                _afterMap.put(colName, colValue);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }

        //最后通过sender方法把row Data投递出去
        sender.sender(rowData);
    }
}
