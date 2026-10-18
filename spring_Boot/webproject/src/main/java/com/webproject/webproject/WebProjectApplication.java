package com.webproject.webproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebProjectApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context= SpringApplication.run(WebProjectApplication.class, args);

		Dev obj= context.getBean(Dev.class);//new Hello();
		obj.build();
	}

}
