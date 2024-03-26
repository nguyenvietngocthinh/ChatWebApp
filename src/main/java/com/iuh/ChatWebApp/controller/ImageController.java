package com.iuh.ChatWebApp.controller;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iuh.ChatWebApp.Service.ImageServiceImpl;
import com.iuh.ChatWebApp.entity.Image;

@Controller
public class ImageController {
	@Autowired
	private ImageServiceImpl imageServiceImpl;

	@PostMapping("/imagesSend/save")
	public ResponseEntity<?> saveImageSend(@ModelAttribute(name = "image") Image image, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		try {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			image.setName(fileName);

			Image savedImage = imageServiceImpl.save(image);

			String uploadDir = "./images-send/";

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				System.out.println(filePath.toFile().getAbsolutePath());
			} catch (IOException e) {
				throw new IOException("Could not save uploaded file: " + fileName);
			}

			ra.addFlashAttribute("messages", "The image upload successfully");

			return ResponseEntity.ok().body("Hình ảnh đã được gửi và lưu xuống cơ sở dữ liệu.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi khi gửi và lưu hình ảnh.");
		}
	}

}
