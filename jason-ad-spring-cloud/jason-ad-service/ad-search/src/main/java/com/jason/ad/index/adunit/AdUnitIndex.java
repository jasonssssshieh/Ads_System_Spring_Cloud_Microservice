package com.jason.ad.index.adunit;

import com.jason.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware <Long, AdUnitObject> {

    private static Map<Long, AdUnitObject> objectMap;
    //<UnitId, UnitObject> 一堆键值pair

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    public Set<Long> match(Integer positionType){//positionType 应该是当做adSlotType 也就是广告位要求的匹配
        //public static boolean isAdSlotTypeOk(int adSlotType, int positionType){}
        //就是给定一个adSlotType, 找到所有符合在这个adSlotType下面能够满足我们的POSITION_TYPE的unit ids
        Set<Long> adUnitIds = new HashSet<>();

        objectMap.forEach((k,v) ->{
            if(AdUnitObject.isAdSlotTypeOk(positionType, v.getPositionType())){
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }

    //上面我们能够检索到所有的adUnitIds by match() 方法, 但是我们还是需要AdUnitObject对象
    //所以这里我们实现一个获取AdUnitObject的方法
    public List<AdUnitObject> fetch(Collection<Long> adUnitIds){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return Collections.emptyList();
        }
        List<AdUnitObject> result = new ArrayList<>();
        adUnitIds.forEach(u -> {
            AdUnitObject object = get(u);
            if(object == null){
                log.error("AdUnitObject not found: {}", u);
                return;
                //return Collections.emptyList();
            }
            result.add(object);
        });
        return result;
    }


    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("before update: {}", objectMap);
        AdUnitObject oldObject = objectMap.get(key);
        if(oldObject == null){
            objectMap.put(key, value);
        }else{
            oldObject.update(value);
        }
        log.info("after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }
}