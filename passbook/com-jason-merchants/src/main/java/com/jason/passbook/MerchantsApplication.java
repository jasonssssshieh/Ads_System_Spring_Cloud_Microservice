package com.jason.passbook;

import com.jason.passbook.security.AuthCheckInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class MerchantsApplication extends WebMvcConfigurerAdapter{

    @Resource
    //注入拦截器
    private AuthCheckInterceptor authCheckInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(MerchantsApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor).addPathPatterns(
                //在某一些path patterns 才会进行拦截
                "/merchants/**"
        );
        super.addInterceptors(registry);//注册拦截器
    }
}
