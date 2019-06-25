package com.jason.ad.mysql.dto;

import com.jason.ad.mysql.constant.OpType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Data
@Component
public class ParseTemplate {

    private String database;//他所监听的数据库, 也就是模板文件template声明的数据库

    /**
     * tabletemplate里面定义的就是我们对各个表的属性的定义
     * 我可以把table template看成template的详细信息jAVA对象版
     * 而template是我们监控数据JSON对象的一个以String类型为主的版本
     * tableTemplateMap 里 key是表的名称
     *
     * ParseTemplate包含了template table的所有信息 以及将他们对象化了
     */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();


    public static ParseTemplate parse(Template _template){
        ParseTemplate template = new ParseTemplate();
        template.setDatabase(_template.getDatabase());
        for (JsonTable jsonTable : _template.getTableList()) {
            String name = jsonTable.getTableName();
            Integer level = jsonTable.getLevel();
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            tableTemplate.setLevel(level.toString());
            template.tableTemplateMap.put(name, tableTemplate);

            //遍历操作类型对应的列
            Map<OpType, List<String>> opFieldSetMap =
                    tableTemplate.getOpTypeFieldSetMap();

            for(JsonTable.Column column : jsonTable.getInsert()){
                getAndCreateIfNeed(
                        OpType.ADD,
                        opFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for(JsonTable.Column column : jsonTable.getUpdate()){
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            for(JsonTable.Column column : jsonTable.getDelete()){
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
        }
        return template;
    }


    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map,
                                               Supplier<R> factory){
        return map.computeIfAbsent(key, k->factory.get());
    }
}
