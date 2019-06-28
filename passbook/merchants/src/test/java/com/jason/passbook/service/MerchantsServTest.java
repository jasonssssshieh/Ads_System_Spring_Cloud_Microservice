package com.jason.passbook.service;

import com.alibaba.fastjson.JSON;
import com.jason.passbook.vo.CreateMerchantsRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 商户服务测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantsServTest {

    @Autowired
    private IMerchantsService merchantsService;

    @Test
    public void testCreateMerchantServ(){

        CreateMerchantsRequest request = new CreateMerchantsRequest();
        request.setName("Jason");
        request.setLogoUrl("www.jason.com");
        request.setBusinessLicenseUrl("www.jason.com");
        request.setPhone("8008208820");
        request.setAddress("NEW YORK, NY");

        System.out.println(JSON.toJSONString(
                merchantsService.createMerchants(request)
        ));
    }
}
