package com.jason.ad.service;


import com.alibaba.fastjson.JSON;
import com.jason.ad.Application;
import com.jason.ad.constant.CommonStatus;
import com.jason.ad.dao.AdPlanRepository;
import com.jason.ad.dao.AdUnitRepository;
import com.jason.ad.dao.CreativeRepository;
import com.jason.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.jason.ad.dao.unit_condition.AdUnitInterestRepository;
import com.jason.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.jason.ad.dao.unit_condition.CreativeUnitRepository;
import com.jason.ad.dump.Dconstant;
import com.jason.ad.dump.table.*;
import com.jason.ad.entity.AdPlan;
import com.jason.ad.entity.AdUnit;
import com.jason.ad.entity.Creative;
import com.jason.ad.entity.unit_condition.AdUnitDistrict;
import com.jason.ad.entity.unit_condition.AdUnitInterest;
import com.jason.ad.entity.unit_condition.AdUnitKeyword;
import com.jason.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {

    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitInterestRepository interestRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;
    /*
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;
    private final AdUnitDistrictRepository districtRepository;
    private final AdUnitInterestRepository interestRepository;
    private final AdUnitKeywordRepository keywordRepository;

    @Autowired
    public DumpDataService(AdPlanRepository planRepository,
                           AdUnitRepository unitRepository,
                           CreativeRepository creativeRepository,
                           CreativeUnitRepository creativeUnitRepository,
                           AdUnitDistrictRepository districtRepository,
                           AdUnitInterestRepository interestRepository,
                           AdUnitKeywordRepository keywordRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
        this.districtRepository = districtRepository;
        this.interestRepository = interestRepository;
        this.keywordRepository = keywordRepository;
    }
    */

    @Test
    public void dumpAdTableData(){
        dumpAdPlanTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_PLAN)
        );
        dumpAdUnitTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT)
        );
        dumpAdCreativeTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_CREATIVE)
        );
        dumpAdUnitCreativeTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_CREATIVE_UNIT)
        );
        dumpAdUnitItTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_IT)
        );
        dumpAdUnitKeywordTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_KEYWORD)
        );
        dumpAdUnitDistrictTable(
                String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT_DISTRICT)
        );

    }

    //把对应的table里的数据导出为json类型的数据
    private void dumpAdPlanTable(String fileName){
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(
                CommonStatus.VALID.getStatus()
        );
        if(CollectionUtils.isEmpty(adPlans)){
            return;
        }

        List<AdPlanTable> planTables = new ArrayList<>();
        adPlans.forEach(p -> planTables.add(
                new AdPlanTable(
                    p.getId(),
                    p.getUserId(),
                    p.getPlanStatus(),
                    p.getStartDate(),
                    p.getEndDate()
                )
        ));

        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){

            for(AdPlanTable planTable : planTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }

        } catch (IOException ioe){
            log.error("dumpAdPlanTable error");
        }
    }

    private void dumpAdUnitTable(String filename){
        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());
        if(CollectionUtils.isEmpty(adUnits)){
            return;
        }
        /**
         *     private Long unitId;
         private Integer unitStatus;
         private Integer positionType;
         private Long planId;
         */
        List<AdUnitTable> unitTables = new ArrayList<>();
        adUnits.forEach(u -> unitTables.add(
                new AdUnitTable(
                        u.getId(),
                        u.getUnitStatus(),
                        u.getPositionType(),
                        u.getPlanId()
                )
        ));
        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitTable unitTable : unitTables){
                writer.write(JSON.toJSONString(unitTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdUnitTable error");
        }
    }

    private void dumpAdCreativeTable(String filename){
        List<Creative> creatives = creativeRepository.findAll();
        if(CollectionUtils.isEmpty(creatives)){
            return;
        }

        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c -> creativeTables.add(
                new AdCreativeTable(
                        c.getId(),
                        c.getName(),
                        c.getType(),
                        c.getMaterialType(),
                        c.getHeight(),
                        c.getWidth(),
                        c.getAuditStatus(),
                        c.getUrl()
                )
        ));

        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeTable creativeTable : creativeTables){
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdCreativeTable error");
        }
    }

    private void dumpAdUnitKeywordTable(String filename){
        List<AdUnitKeyword> unitKeywords = keywordRepository.findAll();
        if(CollectionUtils.isEmpty(unitKeywords)){
            return;
        }

        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();

        unitKeywords.forEach(k ->unitKeywordTables.add(
                new AdUnitKeywordTable(
                        k.getUnitId(),
                        k.getKeyword()
                )
        ));

        Path path = Paths.get(filename);

        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitKeywordTable unitKeywordTable : unitKeywordTables){
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdUnitKeywordTable error");
        }
    }

    private void dumpAdUnitItTable(String filename){
        List<AdUnitInterest> unitInterests = interestRepository.findAll();
        if(CollectionUtils.isEmpty(unitInterests)){
            return;
        }

        List<AdUnitItTable> itTables = new ArrayList<>();
        unitInterests.forEach(i->itTables.add(
                new AdUnitItTable(
                        i.getUnitId(),
                        i.getItTag()
                )
        ));
        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitItTable unitItTable : itTables){
                writer.write(JSON.toJSONString(unitItTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdUnitItTable error");
        }
    }

    private void dumpAdUnitDistrictTable(String filename){
        List<AdUnitDistrict> unitDistricts = districtRepository.findAll();
        if(CollectionUtils.isEmpty(unitDistricts)){
            return;
        }

        List<AdUnitDistrictTable> districtTables = new ArrayList<>();
        unitDistricts.forEach(d -> districtTables.add(
                new AdUnitDistrictTable(
                        d.getUnitId(),
                        d.getState(),
                        d.getCity()
                )
        ));
        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitDistrictTable districtTable : districtTables){
                writer.write(JSON.toJSONString(districtTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdUnitDistrictTable error");
        }
    }

    private void dumpAdUnitCreativeTable(String filename){
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if(CollectionUtils.isEmpty(creativeUnits)){
            return;
        }

        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(d -> creativeUnitTables.add(
                new AdCreativeUnitTable(
                        d.getId(),
                        d.getUnitId()
                )
        ));
        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeUnitTable creativeUnitTable : creativeUnitTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
        } catch (IOException ioe){
            log.error("dumpAdUnitCreativeTable error");
        }
    }
}
