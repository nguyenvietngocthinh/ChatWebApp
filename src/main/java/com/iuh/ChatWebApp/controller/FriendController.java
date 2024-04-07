package com.iuh.ChatWebApp.controller;

import com.iuh.ChatWebApp.Service.FriendServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendController {
	@Autowired
	private FriendServiceImpl friendService;

	/* 
	 * Add Friend
	 * 
	 * */
	
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
	
	
	
	/* 
	 * AcceptFriendRequest
	 * 
	 * */

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
	
	
	
	/* 
	 * Cancel
	 * 
	 * */

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
	
	
	/* 
	 * SearchFriends
	 * 
	 * */
	
	@GetMapping("/searchFriends")
	public String searchUsers(@RequestParam("searchFriendsInput") String searchFriendsInput, Model model, HttpSession session) {
		// Lấy người dùng đã đăng nhập từ session
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (loggedInUser == null) {
			// Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
			return "redirect:/";
		}

		// Thực hiện tìm kiếm người dùng với searchText
		List<User> friendUsers = friendService.searchFriends(searchFriendsInput, loggedInUser.getPhoneNumber());

		// Thêm danh sách người dùng tìm được vào model để hiển thị trên trang
		model.addAttribute("friendUsers", friendUsers);

		// Trả về view hiển thị danh sách người dùng tìm được
		return "FriendUsers";
	}
	
	@GetMapping("/searchFriendsMobile")
    public ResponseEntity<List<User>> searchFriendsMobile(@RequestParam("searchFriendsInput") String searchFriendsInput, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<User> friendUsers = friendService.searchFriends(searchFriendsInput, loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(friendUsers);
    }

	
} 
