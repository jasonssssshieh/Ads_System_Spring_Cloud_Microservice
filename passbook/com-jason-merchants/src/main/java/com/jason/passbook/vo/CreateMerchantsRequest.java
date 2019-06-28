package com.jason.passbook.vo;

/*
创建商户请求对象
 */

import com.jason.passbook.constant.ErrorCode;
import com.jason.passbook.dao.MerchantsDao;
import com.jason.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantsRequest {

    /*商户名称*/
    private String name;

    /*商户logo*/
    private String logoUrl;

    /*营业执照*/
    private String businessLicenseUrl;

    /*商户联系电话*/
    private String phone;

    /*商户地址*/
    private String address;

    /**
     * 验证请求的有效性
     * @param merchantsDao {@link MerchantsDao}
     * @return {@link ErrorCode}
     */
    public ErrorCode validate(MerchantsDao merchantsDao){

        if(merchantsDao.findByName(this.name) != null){
            return ErrorCode.DUPLICATE_NAME;
        }
        if(this.name == null){
            return ErrorCode.EMPTY_NAME;
        }
        if(this.logoUrl == null){
            return ErrorCode.EMPTY_LOGO;
        }
        if(this.businessLicenseUrl == null){
            return ErrorCode.EMPTY_BUSINESS_LICENSE;
        }
        if(this.address == null){
            return ErrorCode.EMPTY_ADDRESS;
        }
        if(this.phone == null){
            return ErrorCode.ERROR_PHONE;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 将请求对象转换为商户对象 这样可以保存到数据库
     * @return {@link Merchants}
     */
    public Merchants toMerchants(){
        Merchants merchants = new Merchants();

        merchants.setName(name);
        merchants.setLogoUrl(logoUrl);
        merchants.setBusinessLicenseUrl(businessLicenseUrl);
        merchants.setPhone(phone);
        merchants.setAddress(address);

        return merchants;
    }
}
