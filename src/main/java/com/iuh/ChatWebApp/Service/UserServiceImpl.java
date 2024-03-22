package com.iuh.ChatWebApp.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.entity.User;
import com.iuh.ChatWebApp.repository.FriendRepository;
import com.iuh.ChatWebApp.repository.UserRepository;



@Service
public class UserServiceImpl {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FriendRepository friendRepository;

    // luu tong tin
    public String saveUser(User user) {
        // Xử lý lưu thông tin user
        userRepository.save(user);
        return "Đăng ký thành công";
    }

    //kiem tra trung sdt
	public String findByPhoneNumberExist(String phoneNumber) {
		if(userRepository.existsByPhoneNumber(phoneNumber) == true) {
			return "Bị trùng số điện thoại";
		}
		return "Đăng ký thành công";
	}
	
	

	public User findByPhoneNumberAndPassword(String phoneNumber, String password) {
		return userRepository.findByPhoneNumberAndPassword(phoneNumber, password);
	}

	
	public List<User> searchUsers(String searchText, String loggedInUserPhoneNumber) {
	    List<User> foundUsers = new ArrayList<>();
	    // Tìm kiếm người dùng theo số điện thoại hoặc tên đầy đủ
	    foundUsers.addAll(userRepository.findByPhoneNumberContainingIgnoreCase(searchText));
	    // Loại bỏ người dùng hiện tại khỏi kết quả tìm kiếm
	    foundUsers.removeIf(user -> user.getPhoneNumber().equals(loggedInUserPhoneNumber));
	    
	    // Kiểm tra mỗi người dùng trong danh sách tìm kiếm
	    for (User user : foundUsers) {
	        // Kiểm tra xem có mối quan hệ bạn bè giữa người dùng hiện tại và người dùng trong danh sách tìm kiếm không
	        Friend existingFriendship1 = friendRepository.findBySenderAndReceiver(loggedInUserPhoneNumber, user.getPhoneNumber());
	        Friend existingFriendship2 = friendRepository.findBySenderAndReceiver(user.getPhoneNumber(), loggedInUserPhoneNumber);
	        
	        
	        if (existingFriendship1 != null || existingFriendship2 != null) {
	            // Nếu đã là bạn bè, đặt trạng thái là "Đã gửi"
	            user.setFriendStatus("Đã gửi");
	        } else {
	            // Nếu chưa là bạn bè, đặt trạng thái là "Add"
	            user.setFriendStatus("Add");
	        }
	    }
	    return foundUsers;
	}

	
	
	public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

	
	
	public boolean checkFriendship(String loggedInUserPhoneNumber, String targetUserPhoneNumber) {
	    Friend existingFriendship1 = friendRepository.findBySenderAndReceiver(loggedInUserPhoneNumber, targetUserPhoneNumber);
	    Friend existingFriendship2 = friendRepository.findBySenderAndReceiver(targetUserPhoneNumber, loggedInUserPhoneNumber);
	    return existingFriendship1 != null || existingFriendship2 != null;
	}


}