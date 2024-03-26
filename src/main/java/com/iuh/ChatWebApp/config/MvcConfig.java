package com.iuh.ChatWebApp.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class MvcConfig implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path imageUploadDir = Paths.get("./images-send");
		String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/images-send/**").addResourceLocations("file:/" + imageUploadPath + "/");
	}
}
