package com.jason.ad.advice;

import com.jason.ad.annotation.IgnoreResponseAdvice;
import com.jason.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//统一的响应拦截

@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("all")
    //改響應是否應該攔截
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
            //如果我们这个methodParameter的注解被这个IgnoreResponseAdvice所标示,那么我们就不想他们被这个Common Response所影响
        }
        if(methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    @SuppressWarnings("all")
    //在写入响应前可以做的操作
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response = new CommonResponse<>(0, "");//0是代表正常
        if(null == o){
            return response;//就是我们的传入的对象就是一个空的,那么就直接返回response
        }else if(o instanceof CommonResponse){
            response = (CommonResponse<Object>) o;//强制把o转成common response类型
        }else{
            response.setData(o);
        }
        return response;
    }
}
