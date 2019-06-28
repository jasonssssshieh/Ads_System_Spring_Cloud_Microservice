package com.jason.passbook.controller;

import com.alibaba.fastjson.JSON;
import com.jason.passbook.service.IMerchantsService;
import com.jason.passbook.vo.CreateMerchantsRequest;
import com.jason.passbook.vo.PassTemplate;
import com.jason.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 外界能够通过http controller去调用服务接口
 * 商户服务controller
 */

@Slf4j
@RestController
@RequestMapping("/merchants")
public class MerchantsCtl {

    /**
     * 商户服务接口
     */
    @Autowired
    private IMerchantsService merchantsService;

    @ResponseBody //将response返回成一个json的字符串,而不是一个web的名称
    @PostMapping("/create")
    public Response createMerchants(@RequestBody CreateMerchantsRequest request){
        log.info("CreateMerchants: {}", JSON.toJSONString(request));
        return merchantsService.createMerchants(request);
    }

    /**
     * {
     "errorCode": null,
     "errorMsg": "",
     "data": {
     "id": 19,
     "name": "Jason",
     "logoUrl": "www.jason.com",
     "businessLicenseUrl": "www.jason.com",
     "phone": "8008208820",
     "address": "NEW YORK, NY",
     "isAudit": false
     }
     }
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/{id}")
    public Response buildMerchantsInfo(@PathVariable Integer id){
        log.info("buildMerchantsInfo: {}", id);
        return merchantsService.buildMerchantsInfoById(id);
    }

    /**
     * CreateMerchants: {"address":"New York","businessLicenseUrl":"www.google.com",
     * "logoUrl":"www.google.com","name":"Google","phone":"123456789"}
     * @param passTemplate
     * @return
     */
    @ResponseBody
    @PostMapping("/drop")
    public Response dropPassTemplate(@RequestBody PassTemplate passTemplate){
        log.info("dropPassTemplate: {}", passTemplate);
        return merchantsService.dropPassTemplate(passTemplate);
    }
}
