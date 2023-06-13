package com.instagram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class InstaBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaBotApplication.class, args);
	}

}
