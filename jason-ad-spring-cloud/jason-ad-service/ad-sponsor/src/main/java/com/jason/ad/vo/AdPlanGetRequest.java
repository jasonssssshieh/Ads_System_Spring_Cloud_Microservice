package com.jason.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/*
如果我们想要获取系统中的Ad Plan 我们需要传递这样的一个请求对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanGetRequest {

    private Long userId;

    private List<Long> ids;

    public boolean validate(){

        return userId != null && !CollectionUtils.isEmpty(ids);
    }
}
