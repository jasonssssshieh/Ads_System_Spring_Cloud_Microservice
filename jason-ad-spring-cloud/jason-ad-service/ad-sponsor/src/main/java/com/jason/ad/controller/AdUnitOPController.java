package com.jason.ad.controller;

import com.alibaba.fastjson.JSON;
import com.jason.ad.exception.AdException;
import com.jason.ad.service.IAdUnitService;
import com.jason.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdUnitOPController {

    @Autowired
    public AdUnitOPController(IAdUnitService adUnitService) {
        this.adUnitService = adUnitService;
    }

    private final IAdUnitService adUnitService;

    @PostMapping("/create/adUnit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException{
        log.info("ad-sponsor: createUnit -> {}",
                JSON.toJSONString(request));

        return adUnitService.createUnit(request);
    }


    @PostMapping("/create/adUnitKeyworld")
    public AdUnitKeywordResponse createUnitKeyworld(@RequestBody AdUnitKeywordRequest request) throws AdException{
        log.info("ad-sponsor: createUnitKeyworld -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnitKeyword(request);
    }

    @PostMapping("/create/adUnitItworld")
    public AdUnitInterestResponse createUnitIt(@RequestBody AdUnitInterestRequest request) throws AdException{
        log.info("ad-sponsor: createUnitInterest -> {}",
                JSON.toJSONString(request));

        return adUnitService.createUnitInterest(request);
    }

    @PostMapping("/create/adUnitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException{
        log.info("ad-sponsor: createUnitDistrict -> {}",
                JSON.toJSONString(request));

        return adUnitService.createUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(
            @RequestBody CreativeUnitRequest request
    ) throws AdException {
        log.info("ad-sponsor: createCreativeUnit -> {}",
                JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }


}
