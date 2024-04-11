package com.iuh.ChatWebApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.ChatWebApp.Service.StorageService;

@RestController
@RequestMapping("/api/file")
public class StorageRestController {
	
	@Autowired
	private StorageService storageServiceImpl;
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "fileImage") MultipartFile file) {
        return new ResponseEntity<>(storageServiceImpl.uploadFile(file), HttpStatus.OK);
    }

}
