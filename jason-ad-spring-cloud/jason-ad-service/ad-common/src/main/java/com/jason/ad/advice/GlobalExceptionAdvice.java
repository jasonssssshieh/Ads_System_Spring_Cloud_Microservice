package com.jason.ad.advice;

import com.jason.ad.exception.AdException;
import com.jason.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    /*在处理的时候,我们会返回一个CommonResponse的对象 这是我们自己定义的;在发生异常的时候 我们会传入两个参数
     */
    @ExceptionHandler(value = AdException.class)
    //我们要对exception进行handler,并且只对value = AdException.class的这个exception进行处理
    public CommonResponse<String> handlerAdException(HttpServletRequest req,
                                                     AdException ex){
        CommonResponse<String> response = new CommonResponse<>(-1,
                "Business Error!");
        response.setData(ex.getMessage());
        return response;
    }
}
