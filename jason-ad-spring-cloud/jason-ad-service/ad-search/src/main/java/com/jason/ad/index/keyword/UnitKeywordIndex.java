package com.jason.ad.index.keyword;

import com.jason.ad.index.IndexAware;
import com.jason.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
//倒排索引
public class UnitKeywordIndex implements IndexAware <String, Set<Long>> {

    private static Map<String, Set<Long>> keywordUnitMap;//倒排索引, 一个关键词 有多个推广单元Unit对应
    private static Map<Long, Set<String>> unitKeywordMap;//正向索引 一个unit 可以对应多个key words

    static{
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    //从key word来查找可能的推广单元unit的集合
    public Set<Long> get(String key) {
        if(StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> result = keywordUnitMap.get(key);
        if(result == null){
            return Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before add: ->{}",
                keywordUnitMap);

        Set<Long> unitIdSet = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(value);

        for(Long unitId : value){
            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.add(key);
        }
        log.info("UnitKeywordIndex, after add: ->{}",
                keywordUnitMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("keyword index can not support update");
        //因为更新的成本会非常高 更新的话 就先删除再add
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before delete: ->{}",
                unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for(Long unitId : value){
            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.remove(key);
        }

        log.info("UnitKeywordIndex, after delete: ->{}",
                unitKeywordMap);
    }

    public boolean match(Long unitId, List<String> keywords){

        //首先能 我么你这个unitKeywordMap里面要包含我们所给的id,
        // 同时这个unitId对应的keywords不为空(这里不是传进来的那个keywords list哈)
        if(unitKeywordMap.containsKey(unitId)
                && org.apache.commons.collections4.CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            return org.apache.commons.collections4.CollectionUtils.isSubCollection(
                    keywords, unitKeywords
            );
            //当且仅当我们keywords是全部都在uniteKeywords里时才返回true
            //(a,b) 当且仅当a是b的子集时才会返回true
        }
        return false;
    }
}
