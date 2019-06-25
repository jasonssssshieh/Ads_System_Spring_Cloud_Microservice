package com.jason.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor

//对AdUnit推广单元的请求
public class AdUnitRequest {

    private Long planId;

    private String unitName;

    private Integer positionType;//广告位的类型:贴片?blabla...

    private Long budget;

    public boolean createValidate(){
        return planId != null && planId > 0 && !StringUtils.isEmpty(unitName)
                && positionType != null && positionType > 0 && budget != null && budget > 0;
    }
}
