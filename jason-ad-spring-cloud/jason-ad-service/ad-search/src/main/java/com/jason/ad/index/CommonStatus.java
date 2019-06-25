package com.jason.ad.index;


import lombok.Getter;

//标记推广单元和推广计划是有效还是无效状态
@Getter
public enum CommonStatus {
    VALID(1, "有效状态"),
    INVALID(0, "无效状态");

    private Integer status;
    private String desc;//status的描述信息

    CommonStatus(Integer status, String desc){
        this.desc = desc;
        this.status = status;
    }
}
