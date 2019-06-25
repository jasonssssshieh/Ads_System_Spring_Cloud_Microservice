package com.jason.ad.handler;


import com.jason.ad.dump.table.*;
import com.jason.ad.index.DataTable;
import com.jason.ad.index.IndexAware;
import com.jason.ad.index.adplan.AdPlanIndex;
import com.jason.ad.index.adplan.AdPlanObject;
import com.jason.ad.index.adunit.AdUnitIndex;
import com.jason.ad.index.adunit.AdUnitObject;
import com.jason.ad.index.creative.CreativeIndex;
import com.jason.ad.index.creative.CreativeObject;
import com.jason.ad.index.creativeunit.CreativeUnitIndex;
import com.jason.ad.index.creativeunit.CreativeUnitObject;
import com.jason.ad.index.district.UnitDistrictIndex;
import com.jason.ad.index.district.UnitDistrictObject;
import com.jason.ad.index.interest.UnitItIndex;
import com.jason.ad.index.interest.UnitItObject;
import com.jason.ad.index.keyword.UnitKeywordIndex;
import com.jason.ad.index.keyword.UnitKeywordObject;
import com.jason.ad.mysql.constant.OpType;
import com.jason.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 *前面写完了我们把数据库的table导入到文件中, 这样我们能够在检索服务中实现数据的加载和构造全量索引
 * 但这里有一个问题 就是我们在写入文件的时候,我们是定义的AdPlanTable这样的一个对象
 * 但我们在构建索引的时候 是AdPlanObject (with AdPlanIndex)
 *
 * 所以我们需要将写入文件的adplan table转变成adplan object
 * =>构造全量索引的handler方法
 *我们可以通过index里面的add update delete 来进行增删改查 => handler应用到index来实现增删改查
 * 1. 索引之间存在层次划分: (User) -> AdPlan(level 2) and AdCreative(level 2)
 *                                  ->AdUnit(level 3)->AdCondition.. 也就是依赖关系的划分
 * 2. 加载全量索引其实是增量索引 "添加" 的一种特殊形式
 */
@Slf4j
@Component
public class AdLevelDataHandler {

    //这里level2的对于其他的索引index服务没有依赖
    public static void handleLevel2(AdPlanTable planTable, OpType type){
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),//获得了adplanindex的index aware
                planObject.getPlanId(),
                planObject,
                type
        );
    }
    public static void handleLevel2(AdCreativeTable creativeTable, OpType type){
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    //这里level3的依赖于level 2
    public static void handleLevel3(AdUnitTable unitTable, OpType type){
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        //这里是AdUnitTable包含了planId这个属性 所以我们通过Adplanindex 索引的get方法来获取这个Adplanobject
        //如果这个object不存在,那么就不应该被加载到当前的索引中, 因为他当前所属的AdPlan 推广计划还没建立
        /*
        * private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;//与之关联的推广计划的索引
        * */
        if(null == adPlanObject){
            log.error("handleLevel3 found AdPlanObject error: {}",
                    unitTable.getPlanId());
            return;
        }
        //这个AdUnitObject 里面本身还包含了一个AdUnitObject的对象
        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),
                unitTable.getUnitId(),
                unitObject,
                type
        );
    }
    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("CreativeUnitIndex not support update");
            return;
        }

        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        if(unitObject == null){
            log.error("handleLevel3 found AdUnitObject error: {}",
                    creativeUnitTable.getUnitId());
            return;
        }

        if(creativeObject == null){
            log.error("handleLevel3 found CreativeObject error: {}",
                    creativeUnitTable.getAdId());
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );

        //<adId-unitId, CreativeUnitObject>
        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(creativeUnitTable.getAdId().toString(),
                        creativeUnitTable.getUnitId().toString()),
                creativeUnitObject,
                type
        );
    }

    //第四层级 level 4
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("district index does not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitDistrictTable.getUnitId());

        if(unitObject == null){
            log.error("handleLevel4 AdUnitDistrictTable found AdUnitObject error: {}", unitDistrictTable.getUnitId());
            return;
        }

        UnitDistrictObject unitDistrictObject = new UnitDistrictObject(
                unitDistrictTable.getState(),
                unitDistrictTable.getCity(),
                unitDistrictTable.getUnitId()
        );
        //<String, Set<Long>>
        String key = CommonUtils.stringConcat(
                unitDistrictTable.getState(),
                unitDistrictTable.getCity()
        );
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                type
        );
    }
    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("keyword index does not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if(unitObject == null){
            log.error("handleLevel4 AdUnitKeywordTable found AdUnitObject error: {}",
                    unitKeywordTable.getUnitId());
            return;
        }

        UnitKeywordObject keywordObject = new UnitKeywordObject(
                unitKeywordTable.getUnitId(),
                unitKeywordTable.getKeyword()
        );

        //<String, Set<Long>>
        String key = unitKeywordTable.getKeyword();
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitKeywordTable.getUnitId())
        );
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class),
                key,
                value,
                type
        );
    }
    public static void handleLevel4(AdUnitItTable unitItTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("AdUnitItIndex does not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if(unitObject == null){
            log.error("handleLevel4 AdUnitItTable found AdUnitObject error: {}",
                    unitItTable.getUnitId());
            return;
        }
        UnitItObject itObject = new UnitItObject(
               unitItTable.getUnitId(),
               unitItTable.getItTag()
        );
        //<String, Set<Long>>
        String key = unitItTable.getItTag();
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handleBinlogEvent(DataTable.of(UnitItIndex.class),
                key,
                value,
                type
        );
    }

    //<K, V> 是因为我们index对象需要一堆键值
    //我们需要对哪个index进行什么type的操作 一个统一的接口, 既可以处理全量索引的更新和维护,也可以实现增量索引的更新与维护
    private static <K, V> void handleBinlogEvent(IndexAware <K, V> index,
                                                 K key,
                                                 V value,
                                                 OpType type) {
        switch (type){
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                    break;
        }
    }
}
