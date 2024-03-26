package com.iuh.ChatWebApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.ChatWebApp.entity.Image;
import com.iuh.ChatWebApp.repository.ImageRepository;

@Service
public class ImageServiceImpl {
	
	@Autowired
	private ImageRepository imageRepository;

	public Image save(Image image) {
		return imageRepository.save(image);
	}

	
}
