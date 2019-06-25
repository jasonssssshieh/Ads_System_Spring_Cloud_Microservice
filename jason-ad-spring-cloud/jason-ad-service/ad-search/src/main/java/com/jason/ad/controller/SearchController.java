package com.jason.ad.controller;


import com.alibaba.fastjson.JSON;
import com.jason.ad.annotation.IgnoreResponseAdvice;
import com.jason.ad.client.SponsorClient;
import com.jason.ad.client.vo.AdPlan;
import com.jason.ad.client.vo.AdPlanGetRequest;
import com.jason.ad.search.ISearch;
import com.jason.ad.search.vo.SearchRequest;
import com.jason.ad.search.vo.SearchResponse;
import com.jason.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {


    @Autowired
    public SearchController(RestTemplate restTemplate,
                            SponsorClient sponsorClient,
                            ISearch search) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
        this.search = search;
    }

    private final RestTemplate restTemplate;
    private final SponsorClient sponsorClient;
    private final ISearch search;


    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request){
        log.info("ad-search: fetchAds -> {}",
                JSON.toJSONString(request));
        return search.fetchAds(request);
    }

    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request){
        log.info("ad-search: getAdPlans -> {}",
                JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }


    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request){
        log.info(
                "ad-search: getAdPlanByRibbon -> {}",
                JSON.toJSONString(request)
        );

        return restTemplate.postForEntity(
                "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,
                CommonResponse.class
        ).getBody();
        //CommonResponse.class提供序列化响应的格式
    }


}








