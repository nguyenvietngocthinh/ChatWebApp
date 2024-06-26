package com.iuh.ChatWebApp.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.ChatWebApp.Service.FriendServiceImpl;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/friend")
public class FriendRestController {

	@Autowired
	private FriendServiceImpl friendService;

	@PostMapping("/addFriendM")
	public ResponseEntity<?> addFriendM(@RequestParam("receiverPhoneNumber") String receiverPhoneNumber,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
		}
		friendService.addFriend(loggedInUser.getPhoneNumber(), receiverPhoneNumber);
		return ResponseEntity.ok("Gửi lời mời thành công");
	}
	
	@PostMapping("/acceptFriendRequestM")
    public ResponseEntity<?> acceptFriendRequestM(@RequestParam("senderPhoneNumber") String friendPhoneNumber, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        friendService.acceptFriendRequest(friendPhoneNumber, loggedInUser.getPhoneNumber());
        return ResponseEntity.ok("Chấp nhận thành công");
    }
	
	@PostMapping("/cancelFriendRequestM")
    public ResponseEntity<?> cancelFriendRequestM(@RequestParam("senderPhoneNumber") String friendPhoneNumber, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        friendService.cancelFriendRequest(friendPhoneNumber, loggedInUser.getPhoneNumber());
        return ResponseEntity.ok("Từ chối kết bạn");
    }
	
	@GetMapping("/searchFriendsM")
    public ResponseEntity<List<User>> searchFriends(@RequestParam("searchFriendsInput") String searchFriendsInput, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<User> friendUsers = friendService.searchFriends(searchFriendsInput, loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(friendUsers);
    }
	
	@GetMapping("/getFriendListM")
	public ResponseEntity<List<User>> getFriendList(HttpSession session) {
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	    List<User> friendList = friendService.getFriendListByPhoneNumber(loggedInUser.getPhoneNumber());
	    return ResponseEntity.ok(friendList);
	}

}
