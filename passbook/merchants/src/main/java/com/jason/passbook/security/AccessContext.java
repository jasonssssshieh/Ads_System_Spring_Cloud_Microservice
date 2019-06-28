package com.jason.passbook.security;

/**
 * 安全校验: 用ThreadLocal去单独存储每一个线程携带的token信息
 */
public class AccessContext {

    private static final ThreadLocal<String> token = new ThreadLocal<String>();

    public static String getToken(){
        return token.get();
    }

    public static void setToken(String tokenStr){
        token.set(tokenStr);
    }

    public static void clearAccessKey(){
        token.remove();//当前线程的remove信息就被remove掉了
    }
}