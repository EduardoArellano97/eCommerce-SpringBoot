package com.example.eCommerceSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.sql.DataSource;

@SpringBootApplication
public class ECommerceSpringBootApplication {

	public static void main(String[] args) {

		SpringApplication.run(ECommerceSpringBootApplication.class, args);
	}

}
