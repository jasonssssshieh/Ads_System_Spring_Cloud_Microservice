package com.jason.passbook.service.Impl;


import com.alibaba.fastjson.JSON;
import com.jason.passbook.constant.Constants;
import com.jason.passbook.constant.ErrorCode;
import com.jason.passbook.dao.MerchantsDao;
import com.jason.passbook.entity.Merchants;
import com.jason.passbook.service.IMerchantsService;
import com.jason.passbook.vo.CreateMerchantsRequest;
import com.jason.passbook.vo.CreateMerchantsResponse;
import com.jason.passbook.vo.PassTemplate;
import com.jason.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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
        Response response = new Response();
        Merchants merchants = merchantsDao.findById(id);
        if(merchants == null){
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXIST.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXIST.getDesc());
        }
        response.setData(merchants);
        return response;
    }

    @Override
    public Response dropPassTemplate(PassTemplate template) {
        Response response = new Response();
        ErrorCode errorCode = template.validate(merchantsDao);
        if(errorCode != ErrorCode.SUCCESS){
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }else{
            String passTemplate = JSON.toJSONString(template);
            System.out.println(passTemplate);
            response.setErrorCode(ErrorCode.SUCCESS.getCode());
            response.setErrorMsg(ErrorCode.SUCCESS.getDesc());

            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplate
            );
            log.info("DropPassTemplate: {}", passTemplate);
        }
        return response;
    }
}
