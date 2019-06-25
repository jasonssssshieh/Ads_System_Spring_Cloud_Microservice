package com.jason.ad.service;

import com.jason.ad.exception.AdException;
import com.jason.ad.vo.*;

public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;
    AdUnitInterestResponse createUnitInterest(AdUnitInterestRequest request) throws AdException;
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;


    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}
