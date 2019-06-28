package com.jason.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    //与hbase的表结构是一致的
    //用户id
    private Long id;

    /**
     * 列族1: 用户基本信息
     */
    private BaseInfo baseInfo;

    /**
     * 列族2: 用户额外信息
     */
    private OtherInfo otherInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BaseInfo{
        private String name;
        private Integer age;
        private String sex;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OtherInfo{
        private String phone;
        private String address;
    }
}
