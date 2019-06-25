package com.jason.ad.service.impl;

import com.jason.ad.constant.Constants;
import com.jason.ad.dao.AdPlanRepository;
import com.jason.ad.dao.AdUnitRepository;
import com.jason.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.jason.ad.dao.unit_condition.AdUnitInterestRepository;
import com.jason.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.jason.ad.dao.unit_condition.CreativeUnitRepository;
import com.jason.ad.entity.AdPlan;
import com.jason.ad.entity.AdUnit;
import com.jason.ad.entity.unit_condition.AdUnitDistrict;
import com.jason.ad.entity.unit_condition.AdUnitInterest;
import com.jason.ad.entity.unit_condition.AdUnitKeyword;
import com.jason.ad.entity.unit_condition.CreativeUnit;
import com.jason.ad.exception.AdException;
import com.jason.ad.service.IAdUnitService;
import com.jason.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {


    //因此我们需要plan的DAO接口和unit的DAO接口
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;
    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitInterestRepository unitInterestRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository,
                             AdUnitRepository unitRepository,
                             AdUnitKeywordRepository unitKeywordRepository,
                             AdUnitInterestRepository unitInterestRepository,
                             AdUnitDistrictRepository unitDistrictRepository,
                             CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitInterestRepository = unitInterestRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());

        if(!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(), request.getUnitName()
        );

        if(oldAdUnit != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        //AdUnit(Long planId, String unitName, Long budget,
        //                  Integer positionType)
        AdUnit newAdUnit = unitRepository.save(
                new AdUnit(request.getPlanId(), request.getUnitName(), request.getBudget(), request.getPositionType())
        );
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request)
            throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        //public AdUnitKeyword(Long unitId, String keyword)
        if(!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(
                    i -> unitKeywords.add(
                            new AdUnitKeyword(i.getUnitId(), i.getKeyword()))
            );
            ids = unitKeywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitInterestResponse createUnitInterest(AdUnitInterestRequest request)
            throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitInterestRequest.UnitInterest::getUnitId).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitInterest> unitIts = new ArrayList<>();
        request.getUnitIts().forEach(i -> unitIts.add(
                new AdUnitInterest(i.getUnitId(), i.getItTag())
        ));
        List<Long> ids = unitInterestRepository.saveAll(unitIts).stream().map(
                AdUnitInterest::getId).collect(Collectors.toList());
        return new AdUnitInterestResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)
            throws AdException {

        //java 8的流式获取数据方法
        List<Long> unitIds = request.getUnitDistricts().stream().map(
                AdUnitDistrictRequest.UnitDistrict::getUnitId
        ).collect(Collectors.toList());
        //校对传进来的unit id是否存在
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitDistrict> unitDistricts = new ArrayList<>();

        //AdUnitDistrict(Long unitId, String state,
        //                          String city)
        request.getUnitDistricts().forEach(i -> unitDistricts.add(
                new AdUnitDistrict(i.getUnitId(), i.getState(), i.getCity())
        ));

        //代表保存之后得到的主键
        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts).stream().map(
                AdUnitDistrict::getId).collect(Collectors.toList());
        return new AdUnitDistrictResponse(ids);
    }


    //验证unit id是否存在
    private boolean isRelatedUnitExist(List<Long> unitIds){

        if(CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        //防止重复 直接比较大小
        return unitRepository.findAllById(unitIds).size() ==
                new HashSet<>(unitIds).size();
    }

    //验证creative id是否存在
    private boolean isRelatedCreativeExist(List<Long> creativeIds){

        if(CollectionUtils.isEmpty(creativeIds)){
            return false;
        }
        //防止重复 直接比较大小
        return creativeUnitRepository.findAllById(creativeIds).size() ==
                new HashSet<>(creativeIds).size();
    }


    @Override
    /*传进来的是两个id,一个是creative id 另外一个是unit id
    所以需要校验这两个id是否存在
    */
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {

        List<Long> creativeIds = request.getUnitItems().stream().map(
                CreativeUnitRequest.CreativeUnitItem::getCreativeId
        ).collect(Collectors.toList());
        List<Long> unitIds = request.getUnitItems().stream().map(
                CreativeUnitRequest.CreativeUnitItem::getUnitId
        ).collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds) || !isRelatedCreativeExist(creativeIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i -> creativeUnits.add(
                new CreativeUnit(i.getCreativeId(), i.getUnitId())
        ));

        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(
                CreativeUnit::getId
        ).collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }
}
