package com.iuh.ChatWebApp.controller;

import com.iuh.ChatWebApp.Service.FriendServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendController {
	@Autowired
	private FriendServiceImpl friendService;

	// Phương thức xử lý yêu cầu thêm bạn
	@PostMapping("/addFriend")
	public String addFriend(@RequestParam("receiverPhoneNumber") String receiverPhoneNumber, HttpSession session) {
		// Lấy người dùng đã đăng nhập từ session
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (loggedInUser == null) {
			// Xử lý trường hợp khi người dùng chưa đăng nhập
			return "redirect:/";
		}
		// Gọi phương thức để thêm bạn
		friendService.addFriend(loggedInUser.getPhoneNumber(), receiverPhoneNumber);
		return "redirect:/showFormHome";
	}

	@PostMapping("/acceptFriendRequest")
	public String acceptFriendRequest(@RequestParam("senderPhoneNumber") String friendPhoneNumber,
			HttpSession session) {
		if (session.getAttribute("loggedInUser") == null) {
			return "redirect:/";
		}

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		// Gọi phương thức để chấp nhận lời mời kết bạn
		friendService.acceptFriendRequest(friendPhoneNumber, loggedInUser.getPhoneNumber());
		
		
		System.out.println(loggedInUser.getRole());
		return "redirect:/showFormHome";
	}

	@PostMapping("/cancelFriendRequest")
	public String cancelFriendRequest(@RequestParam("senderPhoneNumber") String friendPhoneNumber, HttpSession session) {
	    // Lấy người dùng đã đăng nhập từ session
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (loggedInUser == null) {
	        // Xử lý trường hợp khi người dùng chưa đăng nhập
	        return "redirect:/";
	    }
	    // Gọi phương thức để hủy bỏ yêu cầu kết bạn
	    friendService.cancelFriendRequest(friendPhoneNumber, loggedInUser.getPhoneNumber());
	    return "redirect:/showFormHome";
	}
	
	

}
