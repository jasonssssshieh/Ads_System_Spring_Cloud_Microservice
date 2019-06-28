package com.jason.passbook.constant;

import lombok.Getter;

/**
 * 评论类型枚举
 */

@Getter
public enum FeedbackType {

    PASS(1, "针对优惠券的评论"),
    APP(2, "针对卡包 APP 的评论");

    private Integer code;
    private String desc;

    FeedbackType(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
