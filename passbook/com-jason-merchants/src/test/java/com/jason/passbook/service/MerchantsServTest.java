package com.jason.passbook.service;

import com.alibaba.fastjson.JSON;
import com.jason.passbook.constant.TemplateColor;
import com.jason.passbook.vo.CreateMerchantsRequest;
import com.jason.passbook.vo.PassTemplate;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 商户服务测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantsServTest {

    @Autowired
    private IMerchantsService merchantsService;

    /**
     *
     * 没有@Transactional 数据库里面会出现这行记录
     * {"data":{"id":17},"errorMsg":""}
     */
    /*
    {"data":{"id":18},"errorMsg":""}
    但@Transactional了 那么我们在数据库里面找不到 因为会认为是test, 所以会自动回滚, 所以不会出现在数据库
     */
    @Test
    @Transactional
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

    @Test
    //@Transactional
    /**
     * {"data":{"address":"NEW YORK, NY","businessLicenseUrl":"www.jason.com","id":19,"isAudit":false,
     * "logoUrl":"www.jason.com","name":"Jason","phone":"8008208820"},"errorMsg":""}
     */
    /*
        {"errorCode":6,"errorMsg":"商户不存在"}
     */
    public void testBuildMerchantsInfoById(){
       System.out.println(JSON.toJSONString(merchantsService.buildMerchantsInfoById(20)));
    }

    @Test
    public void testDropPassTemplate(){
        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setId(20);
        passTemplate.setTitle("title: Google");
        passTemplate.setSummary("summary: Google");
        passTemplate.setDesc("desc: Google");
        passTemplate.setLimit(10000L);
        passTemplate.setHasToken(false);
        passTemplate.setBackground(TemplateColor.VIOLET.getCode());
        passTemplate.setStart(DateUtils.addDays(new Date(), -10));
        passTemplate.setEnd(DateUtils.addDays(new Date(), 10));

        System.out.println(JSON.toJSONString(
                merchantsService.dropPassTemplate(passTemplate)
        ));
    }
}
