package com.jason.ad.mysql.dto;

//json文件这整个template的对象 包含了表的名字和一系列list of json tables

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    private String database;
    private List<JsonTable> tableList;
}
