package com.jason.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@SpringBootTest
@ComponentScan(basePackages = {"com.jason.ad.search", "com.jason.ad.index", "com.jason.mysql"} )
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}