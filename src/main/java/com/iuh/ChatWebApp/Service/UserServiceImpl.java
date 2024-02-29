package com.iuh.ChatWebApp.Service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.ChatWebApp.entity.User;
import com.iuh.ChatWebApp.repository.UserRepository;



@Service
public class UserServiceImpl {
    
    @Autowired
    private UserRepository userRepository;



    public String saveUser(User user) {
        // Xử lý lưu thông tin user
        userRepository.save(user);
        return "Đăng ký thành công";
    }

	public String findByPhoneNumber(String phoneNumber) {
		if(userRepository.existsByPhoneNumber(phoneNumber) == true) {
			return "Bị trùng số điện thoại";
		}
		return "Đăng ký thành công";
	}

	public User findByPhoneNumberAndPassword(String phoneNumber, String password) {
		return userRepository.findByPhoneNumberAndPassword(phoneNumber, password);
	}

}