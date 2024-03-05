package com.iuh.ChatWebApp.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    // luu tong tin
    public String saveUser(User user) {
        // Xử lý lưu thông tin user
        userRepository.save(user);
        return "Đăng ký thành công";
    }

    //kiem tra trung sdt
	public String findByPhoneNumber(String phoneNumber) {
		if(userRepository.existsByPhoneNumber(phoneNumber) == true) {
			return "Bị trùng số điện thoại";
		}
		return "Đăng ký thành công";
	}
	
	

	public User findByPhoneNumberAndPassword(String phoneNumber, String password) {
		return userRepository.findByPhoneNumberAndPassword(phoneNumber, password);
	}

	
	public List<User> searchUsers(String searchText) {
	    List<User> foundUsers = new ArrayList<>();
	    // Tìm kiếm người dùng theo số điện thoại hoặc tên đầy đủ
	    foundUsers.addAll(userRepository.findByPhoneNumberContainingIgnoreCase(searchText));
//	    foundUsers.addAll(userRepository.findByFullNameContainingIgnoreCase(searchText));
	    return foundUsers;
	}
	
	public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

	
	public User findUserInSearchList(String searchText, String userPhoneNumber) {
        List<User> foundUsers = searchUsers(searchText);
        
        // Lặp qua danh sách tìm kiếm để tìm user có số điện thoại giống với userPhoneNumber
        for (User user : foundUsers) {
            if (user.getPhoneNumber().equals(userPhoneNumber)) {
                return user;
            }
        }
        
        // Trả về null nếu không tìm thấy user trong danh sách
        return null;
    }


}