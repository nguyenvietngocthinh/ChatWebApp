 package com.iuh.ChatWebApp;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ChatWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatWebAppApplication.class, args);
	}

}
