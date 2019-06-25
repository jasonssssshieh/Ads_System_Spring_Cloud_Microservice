package com.jason.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
//在之后我们的广告系统,所有的configuration的请求都会是通过这个WebConfiguration消息转换器进行通用的配置处理
// , 进行一层过滤处理
public class WebConfiguration implements WebMvcConfigurer{

    @Override
    //把我们的java对象转化成我们HTTP的内容, springboot在参数里面提供了多个消息转换器
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.clear();
        //我们只用到一个转换器
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
