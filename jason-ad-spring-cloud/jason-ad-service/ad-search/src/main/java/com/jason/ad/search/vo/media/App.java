package com.jason.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//终端信息
public class App {

    // 应用编码
    private String appCode;

    //应用名称
    private String appName;

    //应用的包名
    private String packageName;

    // 请求应用界面(activity)名称
    private String activityName;
}
