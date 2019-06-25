package com.jason.ad.controller;

import com.alibaba.fastjson.JSON;
import com.jason.ad.entity.AdPlan;
import com.jason.ad.exception.AdException;
import com.jason.ad.service.IAdPlanService;
import com.jason.ad.vo.AdPlanGetRequest;
import com.jason.ad.vo.AdPlanRequest;
import com.jason.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AdPlanOPController {

    private final IAdPlanService adPlanService;
    @Autowired
    public AdPlanOPController(IAdPlanService adPlanService){
        this.adPlanService = adPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: createAdPlan -> {}",
                JSON.toJSONString(request));
        return adPlanService.createAdPlan(request);
    }

    //获取
    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlan(@RequestBody AdPlanGetRequest request) throws AdException{
        log.info("ad-sponsor: getAdPlan -> {}",
                JSON.toJSONString(request));
        return adPlanService.getAdPlanByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: updateAdPlan -> {}",
                JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: deleteAdPlan -> {}",
                JSON.toJSONString(request));
        adPlanService.deleteAdPlan(request);
    }
}
