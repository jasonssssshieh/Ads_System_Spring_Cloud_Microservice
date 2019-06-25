package com.jason.ad.exception;

//就是我们这里的思想就是有些异常是我们需要统一处理的 而有一些异常呢 是我们就是要他们抛出来的
public class AdException extends Exception{
    public AdException(String message){
        super(message);
    }
}