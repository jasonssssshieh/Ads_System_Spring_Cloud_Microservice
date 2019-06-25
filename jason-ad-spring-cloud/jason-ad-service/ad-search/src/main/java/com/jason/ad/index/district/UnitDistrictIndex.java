package com.jason.ad.index.district;

import com.jason.ad.index.IndexAware;
import com.jason.ad.search.vo.feature.DistrictFeature;
import com.jason.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Slf4j
@Component
//实现对检索的增删改查, K代表返回值, V代表index 的键
//<String, Set<Long>>
//<state-city>作为一个key传进来
public class UnitDistrictIndex implements IndexAware <String, Set<Long>> {
    private static Map<String, Set<Long>>  districtUnitMap;
    private static Map<Long, Set<String>> unitDistrictMap;

    static{
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before add -> {}",
                unitDistrictMap);
        Set<Long> unitIds = CommonUtils.getorCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);
        for(Long unitId : value){
            Set<String> districtSet = CommonUtils.getorCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districtSet.add(key);
        }
        log.info("UnitDistrictIndex, after add -> {}",
                unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("district index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before delete -> {}",
                unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getorCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);
        for(Long unitId : value){
            Set<String> districtSet = CommonUtils.getorCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districtSet.remove(key);
        }
        log.info("UnitDistrictIndex, after delete -> {}",
                unitDistrictMap);
    }


    //实现一个match方法, 来看我们当前的索引里面是否有这个adUnitId下的全部的DistrictFeature
    //即传进来的districts 必须是我们当前系统中已有的数据的子集
    public boolean match(Long adUnitId, List<DistrictFeature.StateAndCity> districts){

        if(unitDistrictMap.containsKey(adUnitId) &&
                CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))){

            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);

            List<String> targetDistricts = districts.stream().map(
                    d -> CommonUtils.stringConcat(d.getState(), d.getCity())
            ).collect(Collectors.toList());

            //看看targetDistricts是否是unitDistricts的子集
            return CollectionUtils.isSubCollection(targetDistricts, unitDistricts);
        }
        return false;
    }
}
