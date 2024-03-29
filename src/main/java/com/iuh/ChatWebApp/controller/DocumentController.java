package com.iuh.ChatWebApp.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iuh.ChatWebApp.entity.Document;
import com.iuh.ChatWebApp.repository.DocumentRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DocumentController {

	@Autowired
	private DocumentRepository repo;

	@PostMapping("/fileSend/save")
	private ResponseEntity<?> uploadFile(@RequestParam("document") MultipartFile multipartFile, RedirectAttributes ra)
			throws IOException {
		try {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			Document document = new Document();
			document.setName(fileName);
			document.setContent(multipartFile.getBytes());
			document.setSize(multipartFile.getSize());
			document.setUploadTime(new Date());

			Document savedDocument = repo.save(document);

			ra.addFlashAttribute("fileId", savedDocument.getId());

			return ResponseEntity.ok().body(Map.of("fileId", savedDocument.getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi khi gửi và lưu file.");
		}

	}
	
	@GetMapping("/download")
	public void downloadFile(@Param("id") int id, HttpServletResponse response) throws Exception {
		Optional<Document> result = repo.findById(id);
		if(!result.isPresent()) {
			throw new Exception("Could not find document with ID: " + id);
		}
		
		Document document = result.get();
		
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attrachment; filename=" + document.getName();
	
		response.setHeader(headerKey, headerValue);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		outputStream.write(document.getContent());
		outputStream.close();
	}
	

}
