package com.jason.ad.index.creative;

import com.jason.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long, CreativeObject>{
    //只有一個正向索引
    private static Map<Long, CreativeObject> objectMap;
    static{
        objectMap = new ConcurrentHashMap<>();
    }

    //根据创意id来获取创意对象 这里非常好实现, 因为你可以看到这里我们的IndexAware<Long, CreativeObject>
    //就是id到creative Object的映射
    public List<CreativeObject> fetch(Collection<Long> adIds){
        if(CollectionUtils.isEmpty(adIds)){
            return Collections.emptyList();
        }

        List<CreativeObject> creativeObjects = new ArrayList<>();
        adIds.forEach(id -> {
            CreativeObject object = get(id);
            if(object == null){
                log.error("CreativeObject Not Found: {}", id);
                return;
            }
            creativeObjects.add(object);
        });
        return creativeObjects;
    }


    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("CreativeIndex, before add -> {}", objectMap);

        objectMap.put(key,value);

        log.info("CreativeIndex, after add -> {}", objectMap);
    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("CreativeIndex, before update -> {}", objectMap);

        CreativeObject oldObject = objectMap.get(key);
        if(oldObject == null){
            objectMap.put(key, value);
        }else{
            oldObject.update(value);
        }

        log.info("CreativeIndex, after update -> {}", objectMap);
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("CreativeIndex, before delete -> {}", objectMap);

        objectMap.remove(key);

        log.info("CreativeIndex, after delete -> {}", objectMap);
    }
}
