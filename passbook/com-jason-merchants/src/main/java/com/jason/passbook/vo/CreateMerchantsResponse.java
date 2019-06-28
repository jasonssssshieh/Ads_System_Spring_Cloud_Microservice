package com.jason.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建商户响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantsResponse {

    /*商户id: 返回一个id给商户, 让用户以后通过这个id进行优惠券发布等等
     * 创建失败就返回 -1*/
    private Integer id;

}
