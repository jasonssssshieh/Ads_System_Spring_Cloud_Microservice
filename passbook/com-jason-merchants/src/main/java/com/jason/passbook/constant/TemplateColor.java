package com.jason.passbook.constant;

import lombok.Getter;

/**
 * 优惠券的背景颜色
 */
@Getter
public enum TemplateColor {
    RED(1,"红色"),
    GREEN(2, "绿色"),
    BLUE(3, "蓝色"),
    BLACK(4, "黑色"),
    YELLOW(6, "黄色"),
    VIOLET(7, "紫罗兰色"),
    WHITE(5, "白色");

    /**
     * 背景颜色代码
     */
    private Integer code;
    /*
    颜色描述
     */
    private String color;

    TemplateColor(Integer code, String color){
        this.code = code;
        this.color = color;
    }
}