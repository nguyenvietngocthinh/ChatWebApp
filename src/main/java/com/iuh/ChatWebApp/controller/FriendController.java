package com.iuh.ChatWebApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.iuh.ChatWebApp.Service.FriendServiceImpl;


@Controller
public class FriendController {
	@Autowired
    private FriendServiceImpl friendService;

}
