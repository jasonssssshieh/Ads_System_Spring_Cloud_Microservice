package com.jason.ad.mysql.constant;


import com.github.shyiko.mysql.binlog.event.EventType;

//Mysql的操作类型 和我们的全量索引和增量索引相关
public enum OpType {
   ADD,
   UPDATE,
   DELETE,
   OTHER;

   //为了把EventType转化成为OpType
    public static OpType to(EventType eventType){
        switch (eventType){
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }
    }
}
