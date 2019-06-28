package com.jason.passbook.vo;


/*通用的响应对象*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /*错误码, 正确=0*/
    private Integer errorCode;
    /*错误信息,正确返回空字符串*/
    private String errorMsg = "";

    /*
     * 返回客户端的数据值*/
    private Object data;

    /**
     * 正确的响应构造函数
     * @param data 返回值对象
     */
    public Response(Object data){
        this.data = data;
    }
}

