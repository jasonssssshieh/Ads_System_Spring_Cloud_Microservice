package com.jason.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.jason.ad.index.CommonStatus;
import com.jason.ad.index.DataTable;
import com.jason.ad.index.adunit.AdUnitIndex;
import com.jason.ad.index.adunit.AdUnitObject;
import com.jason.ad.index.creative.CreativeIndex;
import com.jason.ad.index.creative.CreativeObject;
import com.jason.ad.index.creativeunit.CreativeUnitIndex;
import com.jason.ad.index.district.UnitDistrictIndex;
import com.jason.ad.index.interest.UnitItIndex;
import com.jason.ad.index.keyword.UnitKeywordIndex;
import com.jason.ad.search.ISearch;
import com.jason.ad.search.vo.SearchRequest;
import com.jason.ad.search.vo.SearchResponse;
import com.jason.ad.search.vo.feature.DistrictFeature;
import com.jason.ad.search.vo.feature.FeatureRelation;
import com.jason.ad.search.vo.feature.ItFeature;
import com.jason.ad.search.vo.feature.KeywordFeature;
import com.jason.ad.search.vo.media.AdSlot;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service("iSearch")
public class SearchImpl implements ISearch {

    public SearchResponse fallback(SearchRequest request, Throwable e){
        return null;
    }


    @Override
    @HystrixCommand(fallbackMethod = "fallback")//这个fallbackMethod回退方法必须定义在当前的类中
    public SearchResponse fetchAds(SearchRequest request) {

        //请求广告位信息取出
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        //三个feature取出
        KeywordFeature keywordFeature = request.getFeatureInfo().
                getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().
                getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().
                getItFeature();
        FeatureRelation relation = request.getFeatureInfo().
                getRelation();

        //构造响应对象 SearchResponse
        SearchResponse response = new SearchResponse();
        //这里的Creative是在SearchResponse里面定义的一个内部类的Creative
        //<adSlotCode, list of creative>
        //key 就是这个广告位编码
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        //对这个map进行填充
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
            //根据流量类型 获取 初始的AdUnit
            //这里是做了一个预过滤 就是先根据我们广告位的要求进行一个search, 把范围缩小 (接下来会进行第二次过滤)
            // filter出来我们当前和广告位 type match的unit id
            Set<Long> adUnitIdSet = DataTable.of(
                    AdUnitIndex.class
            ).match(adSlot.getPositionType());
            //System.out.println("current adUnitIdSet: " + JSON.toJSONString(adUnitIdSet));
            //根据3个feature对象实行再过滤
            if(relation == FeatureRelation.AND){
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterItTagFeature(adUnitIdSet, itFeature);

                targetUnitIdSet = adUnitIdSet;

            }else{

                targetUnitIdSet = getORRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        itFeature
                );
            }
            //System.out.println("current targetUnitIdSet: " + JSON.toJSONString(targetUnitIdSet));
            //上面根据AdSlots筛选出了 unitId

            //然后根据我们之前实现的fetch方法, 在AdUnitIndex中根据AdUnitId来获取AdUnitObject
            List<AdUnitObject> unitObjects = DataTable.of(
                    AdUnitIndex.class
            ).fetch(targetUnitIdSet);

            //System.out.println("current unitObjects before filterAdUnitAndPlanStatus: " + JSON.toJSONString(unitObjects));
            //再来根据所需要的状态进行过滤 找出VALID的units
            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);
            //System.out.println("current unitObjects after filterAdUnitAndPlanStatus: " + JSON.toJSONString(unitObjects));

            /**根据我们的AdUnitObject来得到全部与之关联的CreativeId
             * 这个获取的方法在之前实现过了, 首先是在UnitCreative关联的Index里面,通过我们的AdUnitObject list来获取creative id list
             * 再通过上一步得到的creative ids在CreativeIndex里面fetch出我们对应的creativeObject
             */
            List<Long> creativeIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);

            //System.out.println("current creativeIds: " + JSON.toJSONString(creativeIds));

            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(creativeIds);

            /**再进行一次过滤, 通过AdSlot实现对creativeObject的过滤.
             * 因为在我们的AdSlot里面, 会有很多广告位的信息,比如宽度 高度 等等
             * 我们需要根据这些信息 来看看是否能匹配我们当前筛选出来的CreativeObject
             */
            //System.out.println("current creativeObjects before filterCreativeByAdSlot: " + JSON.toJSONString(creativeObjects));

            /**
             private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,
             Integer width,
             Integer height,
             List<Integer> type)
             */

            filterCreativeByAdSlot(creativeObjects,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );

            System.out.println("current creativeObjects after filterCreativeByAdSlot: " + JSON.toJSONString(creativeObjects));

            adSlot2Ads.put(
                    adSlot.getAdSlotCode(),
                    buildCreativeResponse(creativeObjects)
            );
        }
        log.info("fetchAds: {}-{}",
                JSON.toJSONString(request),
                JSON.toJSONString(response));
        return response;
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature){

        if(CollectionUtils.isEmpty(adUnitIdSet)){
            return Collections.emptySet();
        }
        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterItTagFeature(districtUnitIdSet, itFeature);

        //去重+Union
        return new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(
                                keywordUnitIdSet, districtUnitIdSet
                        ), itUnitIdSet
                )
        );
    }


    private void filterKeywordFeature(Collection<Long> adUnitIds,
                                      KeywordFeature keywordFeature){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){

            CollectionUtils.filter(
                    adUnitIds,//需要过滤集合
                    adUnitId ->//判断条件 实现for循环 如果是true 那么就继续留在集合中,如果是false 就从集合中移除掉
                            DataTable.of(UnitKeywordIndex.class).match(adUnitId, keywordFeature.getKeywords())
            );
        }
        return;
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds,
                                       DistrictFeature districtFeature){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(UnitDistrictIndex.class).match(
                        adUnitId, districtFeature.getDistricts()
                    )
            );
        }
    }

    private void filterItTagFeature(Collection<Long> adUnits,
                                 ItFeature itFeature){
        if(CollectionUtils.isEmpty(adUnits)){
            return;
        }
        if(CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(
                    adUnits,
                    adUnit -> DataTable.of(UnitItIndex.class).match(adUnit, itFeature.getIts())
            );
        }
    }

    //状态的判断 假如如果是INVALID的状态 那么是不能够被检索出来的
    //而且我们的AdUnit里面是包含了与之对应的AdPlanObject
    //这里我们也传入一个CommonStatus, 这样就不仅仅是说我们只filter出来valid的unit, 而是我们能够filter出和我们要求的status
    //一样的adUnitObject
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects,
                                           CommonStatus status){
        if(CollectionUtils.isEmpty(unitObjects)){
            return;
        }

        CollectionUtils.filter(
                unitObjects,
                unitObject-> unitObject.getUnitStatus().equals(status.getStatus())
                        && unitObject.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );
    }

    //根据AdSlot的广告位信息,来对CreativeObject进行筛选, 要符合对应的宽度 高度等等...
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type){
        if(CollectionUtils.isEmpty(creativeObjects)){
            return;
        }

        CollectionUtils.filter(
                creativeObjects,
                creativeObject->
                      creativeObject.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                && creativeObject.getWidth().equals(width)
                && creativeObject.getHeight().equals(height)
                && type.contains(creativeObject.getType())
        );
    }

    //建立SearchResponse的creative列表
    private List<SearchResponse.Creative> buildCreativeResponse(
            List<CreativeObject> creativeObjects){

        if(CollectionUtils.isEmpty(creativeObjects)){
            return Collections.emptyList();
        }

        //这里随机返回一个creativeObject
        CreativeObject randomObject = creativeObjects.get(
                Math.abs(new Random().nextInt()) % creativeObjects.size()
        );

        return Collections.singletonList(
                SearchResponse.convert(randomObject)
        );
    }
}
