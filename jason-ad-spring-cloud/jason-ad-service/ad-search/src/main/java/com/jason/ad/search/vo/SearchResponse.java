package com.jason.ad.search.vo;
import com.jason.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//请求回复的对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    //<adSlotCode, list of creative>
    //可以有多个creative
    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative {//我们会返回一个广告创意

        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        //展示监测 URL
        private List<String> showMonitorUrl =
                Arrays.asList("www.jasonshieh.com", "www.jasonshieh.com");
        //点击监测 URL
        private List<String> clickMonitorUrl =
                Arrays.asList("www.jasonshieh.com", "www.jasonshieh.com");
    }

    //把索引对象转化成我们的creative对象
    public static Creative convert(CreativeObject object){

        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;
    }
}
