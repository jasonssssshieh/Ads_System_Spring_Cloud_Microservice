package com.jason.ad.service;


import com.jason.ad.Application;
import com.jason.ad.exception.AdException;
import com.jason.ad.vo.AdPlanGetRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService planService;

    @Test
    public void testGetAdPlan() throws AdException {
        System.out.println(
                planService.getAdPlanByIds(
                        new AdPlanGetRequest(15L, Collections.singletonList(10L))
                )
        );
        //[AdPlan(id=10, userId=15, planName=推广计划名称, planStatus=1,
        // startDate=2018-11-28 00:00:00.0, endDate=2019-11-20 00:00:00.0,
        // createTime=2018-11-19 20:42:27.0, updateTime=2018-11-19 20:57:12.0)]
    }
}