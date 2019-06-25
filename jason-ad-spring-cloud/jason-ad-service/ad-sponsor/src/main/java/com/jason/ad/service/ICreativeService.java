package com.jason.ad.service;

import com.jason.ad.vo.CreativeRequest;
import com.jason.ad.vo.CreativeResponse;

public interface ICreativeService {
    CreativeResponse createCreative(CreativeRequest request);
}
