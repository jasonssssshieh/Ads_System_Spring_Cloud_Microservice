package com.jason.passbook.service.Impl;

import com.jason.passbook.constant.ErrorCode;
import com.jason.passbook.dao.MerchantsDao;
import com.jason.passbook.service.IMerchantsService;
import com.jason.passbook.vo.CreateMerchantsRequest;
import com.jason.passbook.vo.CreateMerchantsResponse;
import com.jason.passbook.vo.PassTemplate;
import com.jason.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户服务接口实现
 */
@Slf4j
@Service
public class MerchantsServiceImpl implements IMerchantsService{

    /***
     * merchants 数据接口
     */
    @Autowired
    private MerchantsDao merchantsDao;

    @Override
    @Transactional//只有保存成功才提交 保存失败回滚
    public Response createMerchants(CreateMerchantsRequest request) {
        Response response = new Response();
        CreateMerchantsResponse merchantsResponse = new CreateMerchantsResponse();

        ErrorCode errorCode = request.validate(merchantsDao);
        if(errorCode != ErrorCode.SUCCESS){
            merchantsResponse.setId(-1);//直接返回错误
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }else{
            merchantsResponse.setId(merchantsDao.save(request.toMerchants()).getId());
        }
        response.setData(merchantsResponse);
        return response;
    }

    @Override
    public Response buildMerchantsInfoById(Integer id) {
        return null;
    }

    @Override
    public Response dropPassTemplate(PassTemplate template) {
        return null;
    }
}
