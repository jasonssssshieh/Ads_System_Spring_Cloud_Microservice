package com.jason.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.jason.ad.dump.table.*;
import com.jason.ad.handler.AdLevelDataHandler;
import com.jason.ad.index.DataLevel;
import com.jason.ad.mysql.constant.Constant;
import com.jason.ad.mysql.dto.MySqlRowData;
import com.jason.ad.sender.ISender;
import com.jason.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("indexSender")
//把MySqlData变成一个TableData类型
public class IndexSender implements ISender{


    @Override
    public void sender(MySqlRowData rowData) {
        String level = rowData.getLevel();

        if(DataLevel.LEVEL2.getLevel().equals(level)){
            //第二层级
            Level2RowData(rowData);
        }else if(DataLevel.LEVEL3.getLevel().equals(level)){
            //第3层级
            Level3RowData(rowData);
        }else if(DataLevel.LEVEL4.getLevel().equals(level)){
            ////第4层级
            Level4RowData(rowData);
        }else{
            log.error("MysqlRowData Error: {}", JSON.toJSONString(rowData));
        }
    }

    private void Level2RowData(MySqlRowData rowData){

        //构建增量索引
        //需要用到AdLevelHandler里面的各个handle方法
        if(rowData.getTableName().equals(
                Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)){
            //如果是AD_PLAN_TABLE_INFO这张表
            List<AdPlanTable> planTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {
                AdPlanTable planTable = new AdPlanTable();
                //k是colname, v是列的值
                fieldValueMap.forEach((k, v) -> {
                    switch (k){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(
                                    CommonUtils.parseStringDate(v)
                            );
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(
                                    CommonUtils.parseStringDate(v)
                            );
                    }
                });
                planTables.add(planTable);
            }

            planTables.forEach(p-> AdLevelDataHandler.handleLevel2(p,
                    rowData.getOpType()));
            return;
        }
        //如果是AD_CREATIVE_TABLE 这张表
        if(rowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeTable> creativeTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdCreativeTable creativeTable = new AdCreativeTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k){
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(v);
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            }
            creativeTables.forEach(p-> AdLevelDataHandler.handleLevel2(
                    p,
                    rowData.getOpType()
            ));
        }
    }

    private void Level3RowData(MySqlRowData rowData){
        //如果是creative unit table的话
        if(rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                fieldValueMap.forEach((k,v) -> {
                    switch (k){
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }
            creativeUnitTables.forEach(p->AdLevelDataHandler.handleLevel3(
                    p,
                    rowData.getOpType()
            ));
            return;
        }

        //如果是unit table
        if(rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdUnitTable> unitTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdUnitTable unitTable = new AdUnitTable();
                fieldValueMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                    }
                });
                unitTables.add(unitTable);
            }

            unitTables.forEach(p->AdLevelDataHandler.handleLevel3(
                    p,
                    rowData.getOpType()
            ));
            return;
        }
    }

    private void Level4RowData(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME)){
            //interest 限制表
            List<AdUnitItTable> unitItTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdUnitItTable unitItTable = new AdUnitItTable();
                fieldValueMap.forEach((k, v)->{
                    switch (k){
                        case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitItTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                            unitItTable.setItTag(v);
                            break;
                    }
                });
                unitItTables.add(unitItTable);
            }
            unitItTables.forEach(p->AdLevelDataHandler.handleLevel4(
                    p,
                    rowData.getOpType()
            ));
            return;
        }

        if(rowData.getTableName().equals(Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME)){
            List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdUnitKeywordTable unitKeywordTable = new AdUnitKeywordTable();
                fieldValueMap.forEach((k,v) -> {
                    switch (k){
                        case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                            unitKeywordTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                            unitKeywordTable.setKeyword(v);
                            break;
                    }
                });
                unitKeywordTables.add(unitKeywordTable);
            }
            unitKeywordTables.forEach(p->AdLevelDataHandler.handleLevel4(
                    p,
                    rowData.getOpType()
            ));
            return;
        }

        if(rowData.getTableName().equals(Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME)){
            List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdUnitDistrictTable unitDistrictTable = new AdUnitDistrictTable();
                fieldValueMap.forEach((k,v) -> {
                    switch (k){
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_STATE:
                            unitDistrictTable.setState(v);
                            break;
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                            unitDistrictTable.setCity(v);
                            break;
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitDistrictTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                unitDistrictTables.add(unitDistrictTable);
            }
            unitDistrictTables.forEach(p->AdLevelDataHandler.handleLevel4(
                    p,
                    rowData.getOpType()
            ));
            return;
        }
    }
}
