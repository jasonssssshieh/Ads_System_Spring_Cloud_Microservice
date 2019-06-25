package com.jason.ad.constant;

import lombok.Getter;

@Getter
public enum CreativeMaterialType {

    JPG(1, "jpg"),
    BMP(2, "bmp"),

    MP4(3, "mp4"),
    AVI(4, "avi"),

    TXT(5, "txt");

    private int type;
    private String desc;//描述信息

    CreativeMaterialType(int type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
