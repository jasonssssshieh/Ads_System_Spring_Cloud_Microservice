package com.jason.passbook.service;


import com.jason.passbook.vo.CreateMerchantsRequest;
import com.jason.passbook.vo.PassTemplate;
import com.jason.passbook.vo.Response;

/**
 * 对商户服务 接口的定义
 */
public interface IMerchantsService {

    /**
     * 创建商户服务
     * @param request 创建商户请求 {@link CreateMerchantsRequest}
     * @return {@link Response}
     */
    Response createMerchants(CreateMerchantsRequest request);

    /**
     * 根据id构造商户信息
     * @param id 商户id
     * @return {@link Response}
     */
    Response buildMerchantsInfoById(Integer id);

    /**
     * 投放优惠券
     * @param template {@link PassTemplate} 优惠券对象
     * @return Response {@link Response}
     */
    Response dropPassTemplate(PassTemplate template);
}