package com.iuh.ChatWebApp.controller;

import com.iuh.ChatWebApp.Service.FriendServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FriendController {
	@Autowired
	private FriendServiceImpl friendService;

	@PostMapping("/addFriend")
	public String addFriend(@RequestParam("receiverPhoneNumber") String receiverPhoneNumber, HttpSession session) {
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Handle case when user is not logged in
	        return "redirect:/";
	    }
	    friendService.addFriend(loggedInUser.getPhoneNumber(), receiverPhoneNumber);
	    return "redirect:/showFormHome";
	}


}
