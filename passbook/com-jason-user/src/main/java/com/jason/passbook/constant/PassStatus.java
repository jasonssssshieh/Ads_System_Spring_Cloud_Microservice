package com.jason.passbook.constant;

import lombok.Getter;

/**
 * 优惠券状态 枚举类
 *
 */
@Getter
public enum PassStatus {
    UNUSED(1, "未被使用"),
    USED(2, "已被使用"),
    ALL(3, "全部领取的");

    /*
    状态码
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String desc;

    PassStatus(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
