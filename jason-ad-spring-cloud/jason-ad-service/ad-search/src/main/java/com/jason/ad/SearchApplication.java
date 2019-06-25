package com.jason.ad;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients //使用Feign去访问其他的微服务
@EnableEurekaClient
@EnableHystrix //使用断路器
@EnableCircuitBreaker//同上
@EnableDiscoveryClient
@EnableHystrixDashboard
@SpringBootApplication
public class SearchApplication {
    public static void main(String[] args){
        SpringApplication.run(SearchApplication.class, args);
    }

    //定义rest客户端

    @Bean
    @LoadBalanced//能够开启负载均衡的能力 也就是说我们部署的广告投放系统有多个实例的话,
    //那么他可以实现轮询, 以实现复杂均衡
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
