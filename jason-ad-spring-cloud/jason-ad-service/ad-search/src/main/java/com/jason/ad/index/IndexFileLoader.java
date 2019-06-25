package com.jason.ad.index;


import com.alibaba.fastjson.JSON;
import com.jason.ad.dump.Dconstant;
import com.jason.ad.dump.table.*;
import com.jason.ad.handler.AdLevelDataHandler;
import com.jason.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据数据表得出的文件,去读取文件,加载全量索引
 */
@Component
@DependsOn("dataTable")
public class IndexFileLoader {
    //全量索引的加载, 应该在检索系统启动的时候完成

    @PostConstruct
    public void init(){
        //数据加载过程有顺序 按照level来 因为有依赖关系 顺序要求非常严格!!!!!
        //level 2
        //AdPlan
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_PLAN)
        );
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdPlanTable.class),//反序列化
                OpType.ADD
        ));
        //AdCreative
        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(p->AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdCreativeTable.class),
                OpType.ADD
        ));

        //level 3
        //AdUnit
        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT)
        );
        adUnitStrings.forEach(p->AdLevelDataHandler.handleLevel3(
                JSON.parseObject(p, AdUnitTable.class),
                OpType.ADD
        ));

        //AdCreativeUnit
        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(p->AdLevelDataHandler.handleLevel3(
                JSON.parseObject(p, AdCreativeUnitTable.class),
                OpType.ADD
        ));

        //level 4
        //AdUnit_District
        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(p->AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        //AdUnit_Interest
        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(p->AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p, AdUnitItTable.class),
                OpType.ADD
        ));

        //AdUnit_Keyword
        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(p->AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p, AdUnitKeywordTable.class),
                OpType.ADD
        ));


    }


    //因为我们数据就是json类型存的string 所以这里是返回的一个list of string
    private List<String> loadDumpData(String fileName){
        Path path = Paths.get(fileName);
        try(BufferedReader reader = Files.newBufferedReader(path)){
            return reader.lines().collect(Collectors.toList());
            //这个reader.lines返回的是string of string 所以我们可以把它换成list
        } catch (IOException ioe){
            throw new RuntimeException(ioe.getMessage());
        }
    }
}
