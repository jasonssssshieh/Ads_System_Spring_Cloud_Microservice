package com.jason.ad.mysql;


import com.alibaba.fastjson.JSON;
import com.jason.ad.mysql.constant.OpType;
import com.jason.ad.mysql.dto.ParseTemplate;
import com.jason.ad.mysql.dto.TableTemplate;
import com.jason.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {

    @Autowired
    private ParseTemplate template;

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private String SQL_SCHEMA = "select table_schema, table_name, column_name, " +
            "ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    //加载配置文件json的 启动容器的时候就应该允许

    @PostConstruct
    private void init(){
        loadJson("template.json");
    }


    //对外服务的一个方法
    public TableTemplate getTable(String tableName){
        return template.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path){
        //通过这个cl能获得这个path下的文件
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        //获取输入流
        InputStream inStream = cl.getResourceAsStream(path);

        try{
            Template template = JSON.parseObject(
                    inStream,
                    Charset.defaultCharset(),
                    Template.class//反序列化的类定义
            );
            this.template = ParseTemplate.parse(template);
            loadMeta();
        } catch  (IOException ioe){
            log.error(ioe.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }


    //取数据库里查询
    private void loadMeta(){
        for(Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()){
            TableTemplate table = entry.getValue();

            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );
            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (rs, i)->{
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");

                if((null!= updateFields && updateFields.contains(colName))
                        || (deleteFields != null && deleteFields.contains(colName))
                        ||(insertFields != null && insertFields.contains(colName))){
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}
