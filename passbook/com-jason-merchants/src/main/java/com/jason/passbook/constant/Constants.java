package com.jason.passbook.constant;

/**
 * <h1>普通(通用) 常量定义</h1>
 */
public class Constants {

    /*
     * 商户优惠券投放的kafka topic
     * */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /**
     * token string 一个通用的商户token
     */
    public static final String TOKEN_STRING = "token";

    /**
     * token info 和前面的TOKEN_STRING(key)组成一个key-value pair
     */
    public static final String TOKEN = "jason-passbook-merchants";
}
