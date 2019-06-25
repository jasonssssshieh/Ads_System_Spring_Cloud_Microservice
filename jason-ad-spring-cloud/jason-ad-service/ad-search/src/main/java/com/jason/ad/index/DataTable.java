package com.jason.ad.index;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//索引服务 因为我们的索引服务的这些类太多了
// 我们使用的时候一个个去注入特别麻烦,所以我们就现在建立这样的一个类
// 只需要注入一次 我们就能使用所有Index服务.

/**
 * Aware: "我/引用程序想要什么"
 * PriorityOrdered 优先级排序, spring容器在初始化我们的这个beans的时候, 可以定义一些初始化的顺序, spring会先把这些有order的bean先初始化
 * 然后再去初始化其他的
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    //Class代表了我们要使用哪个索引类型
    public static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext
                                                  applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;//值越小,优先级越高
    }

    //DataTable.of(CreativeUnitIndex.class) 就可以获得CreativeUnitIndex 的索引的服务了

    /*
    获取缓存的方法
     */
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz){
        T instance = (T) dataTableMap.get(clazz);
        if(null != instance){
            return instance;
        }
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    /*
    两种不同的获取bean的方式: 通过bean的名字, 和通过class类型
     */
    @SuppressWarnings("all")
    private static <T> T bean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings("all")
    private static <T> T bean(Class clazz){
        System.out.println(JSON.toJSONString(clazz.getName()));
        return (T) applicationContext.getBean(clazz);
    }
}
