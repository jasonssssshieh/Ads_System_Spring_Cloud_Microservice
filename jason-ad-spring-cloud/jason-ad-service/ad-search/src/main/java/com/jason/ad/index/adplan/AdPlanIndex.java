package com.jason.ad.index.adplan;

import com.jason.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//索引实现类
@Slf4j
@Component
public class AdPlanIndex implements IndexAware <Long, AdPlanObject> {

    private static Map<Long, AdPlanObject> objectMap;

    static {
        //为了线程安全 使用ConcurrentHashMap 而不是hash map
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update: {}", objectMap);
        AdPlanObject oldObject = objectMap.get(key);
        if(oldObject == null){
            objectMap.put(key, value);
        }else{
            oldObject.update(value);
        }
        log.info("after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", objectMap);

        objectMap.remove(key);

        log.info("after delete: {}", objectMap);
    }
}