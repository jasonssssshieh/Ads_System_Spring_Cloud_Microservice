package com.jason.ad.index.creativeunit;


import com.jason.ad.index.IndexAware;
import com.jason.ad.index.adunit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware <String, CreativeUnitObject> {

    //<adId-unitId, CreativeUnitObject>
    private static Map<String, CreativeUnitObject> objectMap;

    //<adId, set<unitId>> 创意与广告单元是多对多的关系
    private static Map<Long, Set<Long>> creativeUnitMap;

    //<unitId, set<adId>> 推广单元的id和多个创意的id
    private static Map<Long, Set<Long>> unitCreativeMap;
    static{
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before add ->{}", objectMap);

        objectMap.put(key, value);
        //其实这里相当于前面的index的大集合... 对于objectMap而言,我们只需要put就行,
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(), unitSet);
        }
        unitSet.add(value.getUnitId());


        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isEmpty(creativeSet)){
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(), creativeSet);
        }
        creativeSet.add(value.getAdId());

        log.info("CreativeUnitIndex, after add ->{}", objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {

        log.error("CreativeUnitIndex does not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before delete ->{}", objectMap);

        objectMap.remove(key);

        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isNotEmpty(unitSet)){
            unitSet.remove(value.getUnitId());
        }

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(creativeSet)) {
            creativeSet.remove(value.getAdId());
        }
        log.info("CreativeUnitIndex, after delete ->{}", objectMap);
    }
    ////adId-unitId 結合在一起能唯一確定. 作為一個string的key <adId-unitId>


    //通过单元object对象 返回创意ids creativeids
    //以后那么我们可以在CreativeIndex里面, 通过这些创意的ids来得到creative objects
    public List<Long> selectAds(List<AdUnitObject> unitObjects){
        if(CollectionUtils.isEmpty(unitObjects)){
            return Collections.emptyList();
        }

        List<Long> creativeIds = new ArrayList<>();

        for (AdUnitObject unitObject : unitObjects) {
            //通过unit id 得到了创意的ids
            Set<Long> adIds = unitCreativeMap.get(unitObject.getUnitId());
            if(CollectionUtils.isNotEmpty(adIds)){
                creativeIds.addAll(adIds);
            }
        }

        return creativeIds;
    }
}
