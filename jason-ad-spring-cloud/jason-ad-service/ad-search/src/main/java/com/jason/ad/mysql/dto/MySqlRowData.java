package com.jason.ad.mysql.dto;

import com.jason.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
//因为之前我们从日志中解析出来的BinlogRowData数据比较多 不便于我们解析成为增量索引
//所以这里我们就自己再来构建一个对象 MySqlRowData 去记录我们需要的内容 以便后面我们去进行增量索引的添加以投递
//同时也要讲eventType转化成我们自己定义的那个OpType那个枚举类
public class MySqlRowData {

    //private String dbName; //支持多数据库需要db name
    private String tableName;

    private String level;//数据表的层级

    private OpType opType;

    //其实就是BinlogRowData的afterMap
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();


}
