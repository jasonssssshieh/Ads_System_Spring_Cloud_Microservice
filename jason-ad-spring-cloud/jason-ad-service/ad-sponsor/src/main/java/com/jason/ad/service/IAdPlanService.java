package com.jason.ad.service;

import com.jason.ad.entity.AdPlan;
import com.jason.ad.exception.AdException;
import com.jason.ad.vo.AdPlanGetRequest;
import com.jason.ad.vo.AdPlanRequest;
import com.jason.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {

    /**
     * 创建推广计划
     * @param adPlanRequest
     * @return
     * @throws AdException
     */
    AdPlanResponse createAdPlan(AdPlanRequest adPlanRequest) throws AdException;

    /**
     * 获取推广计划
     * @param adPlanGetRequest
     * @return
     * @throws AdException
     */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest adPlanGetRequest) throws AdException;

    /**
     * 更新推广计划
     * @param adPlanRequest
     * @return
     * @throws AdException
     */
    AdPlanResponse updateAdPlan(AdPlanRequest adPlanRequest) throws AdException;

    /**
     * 删除推广计划
     * @param adPlanRequest
     * @throws AdException
     */
    void deleteAdPlan(AdPlanRequest adPlanRequest) throws AdException;
}
