package com.jason.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//ad plan在创建和更新的响应
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanResponse {

    private Long id;
    private String planName;

}
