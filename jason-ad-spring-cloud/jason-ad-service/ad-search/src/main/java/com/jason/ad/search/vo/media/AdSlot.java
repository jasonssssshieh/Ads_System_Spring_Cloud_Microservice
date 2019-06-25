package com.jason.ad.search.vo.media;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//广告位信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {

    //广告位的编码
    private String adSlotCode;

    //广告位的流量的类型
    private Integer positionType;

    //广告位的宽和高
    private Integer width;
    private Integer height;

    //广告的物料类型 可以有多个类型:图片 视频等等
    private List<Integer> type;

    //价格 最低出价
    private Integer minCpm;
}