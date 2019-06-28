package com.jason.passbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MerchantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantsApplication.class, args);
	}

}
