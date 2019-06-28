package com.jason.passbook.vo;

import com.google.common.base.Enums;
import com.jason.passbook.constant.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评论表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论类型
     */
    private String type;

    /**
     * pass template rowkey,
     * 如果是app 类型的评论, 则没有.
     */
    private String templateId;

    /**
     * 评论内容
     */
    private String comment;

    public boolean validate(){
        FeedbackType feedbackType = Enums.getIfPresent(
                FeedbackType.class,
                this.type.toUpperCase()
        ).orNull();

        return feedbackType != null && comment != null;
    }
}
