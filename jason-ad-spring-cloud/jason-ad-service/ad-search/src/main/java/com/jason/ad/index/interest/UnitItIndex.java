package com.jason.ad.index.interest;

import com.jason.ad.index.IndexAware;
import com.jason.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitItIndex implements IndexAware <String, Set<Long>>{
    //key 是兴趣这个标签, value是unitId Set

    //<itTag, adUnitId set>
    private static Map<String, Set<Long>> itUnitMap;//反向索引

    //<adUnitId, itTag Set>
    private static Map<Long, Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitItIndex, before add -> {}", unitItMap);

        Set<Long> unitIds = CommonUtils.getorCreate(
                key, itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);
        //到这里为止, 你还仅仅只是更新了itUnitMap
        // 下面还需要更新unitItMap 这也是为什么我们log里去打印unitItMap的原因
        for(Long unitId : value){
            Set<String> itTagMap = CommonUtils.getorCreate(
                    unitId, unitItMap,
                    ConcurrentSkipListSet::new
            );
            itTagMap.add(key);
        }
        log.info("UnitItIndex, after add -> {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        //因为更新的成本会非常高 更新的话 就先删除再add
        log.error("interest index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        //先更新逆向map
        log.info("unitItIndex, before delete -> {}", unitItMap);
        Set<Long> unitIds = CommonUtils.getorCreate(
                key, itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);
        //更新正序的map 也就是每一个unitId对应的那个itTag的set
        for(Long unitId : value){
            Set<String> itTagMap = CommonUtils.getorCreate(
                    unitId, unitItMap,
                    ConcurrentSkipListSet::new
            );
            itTagMap.remove(key);
        }
        log.info("unitItIndex, after delete -> {}", unitItMap);
    }

    public boolean match(Long unitId, List<String> itTags){
        //match 看看我们unitId里面的所有的tags 是否是it tags的母集
        if(unitItMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitItMap.get(unitId))){
            Set<String> itTagSet = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags, itTagSet);
        }
        return false;
    }
}
