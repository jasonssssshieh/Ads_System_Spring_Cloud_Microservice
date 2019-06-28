package com.jason.passbook.vo;
/**
 * 用户领取的优惠券
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pass {

    /**
     * 所属用户id
     */
    private Long userId;

    /**
     * pass在HBase中的RowKey
     */
    private String rowKey;

    /**
     * PassTemplate 在HBase中的row key
     */
    private String templateId;

    /**
     * 优惠券token, 有可能是null 则填充-1
     */
    private String token;

    /**
     * 优惠券领取日期
     */
    private Date assignedDate;

    /**
     * 优惠券消费日期 如果不为空 则被消费了
     */
    private Date conDate;
}
