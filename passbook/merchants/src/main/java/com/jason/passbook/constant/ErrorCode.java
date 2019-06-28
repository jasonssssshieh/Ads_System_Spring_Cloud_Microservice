package com.jason.passbook.constant;

import lombok.Getter;

/**
 * 错误编码枚举定义
 */

@Getter
public enum ErrorCode {

    SUCCESS(0, ""),
    DUPLICATE_NAME(1, "商户名称重复"),
    EMPTY_LOGO(2,"商户logo为空"),
    EMPTY_BUSINESS_LICENSE(3, "商户营业执照为空"),
    ERROR_PHONE(4, "商户联系电话错误"),
    EMPTY_ADDRESS(5, "商户地址为空"),
    MERCHANTS_NOT_EXIST(6, "商户不存在"),
    EMPTY_NAME(7, "没有传入商户名称");

    private Integer code;
    private String desc;

    ErrorCode(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
