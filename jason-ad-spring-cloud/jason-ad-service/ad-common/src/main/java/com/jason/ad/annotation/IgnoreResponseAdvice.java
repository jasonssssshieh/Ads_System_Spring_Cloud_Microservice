package com.jason.ad.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//我们会ignore的响应注解,就是有些响应我们没必要去把它们都经过common response
@Target({ElementType.TYPE, ElementType.METHOD})//可以响应在type 也可以在method上
@Retention(RetentionPolicy.RUNTIME)//在运行时进行
public @interface IgnoreResponseAdvice { }
