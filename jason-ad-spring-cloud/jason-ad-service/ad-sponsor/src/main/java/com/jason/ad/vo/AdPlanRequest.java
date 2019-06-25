package com.jason.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

//既可以用于创建新的ad plan request 也可以用于更新ad plan
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanRequest {

    private Long id;

    private Long userId;

    private String planName;

    private String startDate;//这里不用date类型 我们在Utils的common untils里面自己定义了一个方向 把string类型转化成date类型

    private String endDate;

    public boolean createValidate(){

        return userId != null && !StringUtils.isEmpty(planName)
                && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate(){

        return userId != null && userId != null;
    }

    public boolean deleteValidate(){

        return id != null && userId != null;
    }
}
