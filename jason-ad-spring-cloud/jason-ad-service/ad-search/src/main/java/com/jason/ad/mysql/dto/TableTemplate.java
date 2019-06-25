package com.jason.ad.mysql.dto;


import com.jason.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//定义这个table template是为了将来操作的时候 读取一些表的信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    //这里我们把操作类型 和 所需要记录的列定义成一个map的形式
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /*

    字段的索引到字段名的映射
    字段的索引 -> 字段名
    因为在Bin Log里面他不会表达出这个列的名字是什么,他只会显示0 1 2 这样的索引
     */
    private Map<Integer, String> posMap = new HashMap<>();
}
