package com.jason.ad.client;

import com.jason.ad.client.vo.AdPlan;
import com.jason.ad.client.vo.AdPlanGetRequest;
import com.jason.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

//fallback指定了服务降级,
// 一旦这个ad sponsor这个服务不可用呢,
// 那么我们调用getAdPlans这个接口的时候
// 实际上我们会返回SponsorClientHystrix的实现的getAdPlans的方法
/*
* 一般会和feign一起使用
* 因为单独使用hystrix效率会非常低 一般企业级开发中是很少用的
* 因此 我们一般用feign去调用其他微服务的接口,如果出现异常,那么就会调用fallback方法
* */
@FeignClient(value = "eureka-client-ad-sponsor", fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/adPlan",
            method = RequestMethod.POST)
    CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request
            );
}
