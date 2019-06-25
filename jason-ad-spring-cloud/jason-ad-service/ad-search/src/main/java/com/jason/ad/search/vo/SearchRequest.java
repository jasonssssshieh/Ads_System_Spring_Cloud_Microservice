package com.jason.ad.search.vo;


import com.jason.ad.search.vo.feature.DistrictFeature;
import com.jason.ad.search.vo.feature.FeatureRelation;
import com.jason.ad.search.vo.feature.ItFeature;
import com.jason.ad.search.vo.feature.KeywordFeature;
import com.jason.ad.search.vo.media.AdSlot;
import com.jason.ad.search.vo.media.App;
import com.jason.ad.search.vo.media.Device;
import com.jason.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//媒体方请求search的request对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    //媒体方请求的标识
    private String mediaId;

    //请求基本信息
    private RequestInfo requestInfo;

    //请求匹配信息
    private FeatureInfo featureInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo{

        private String requestId;//请求的唯一id

        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo{
        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private ItFeature itFeature;
        private FeatureRelation relation = FeatureRelation.AND;
    }
}
