package com.jason.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    //设备的编码/id
    private String deviceCode;

    //设备的MAC地址
    private String mac;

    //设备的ip
    private String ip;

    //机型编号
    private String model;

    //分辨率尺寸
    private String displaySize;

    //屏幕尺寸
    private String screenSize;

    //设备序列号
    private String serialName;
}
