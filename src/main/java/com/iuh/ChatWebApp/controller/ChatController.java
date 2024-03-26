package com.iuh.ChatWebApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.iuh.ChatWebApp.Service.ChatMessageServiceImpl;
import com.iuh.ChatWebApp.Service.ChatRoomServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.ChatMessage;
import com.iuh.ChatWebApp.entity.ChatNotification;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {
	@Autowired
	private ChatMessageServiceImpl chatMessageServiceImpl;

	@Autowired
	private ChatRoomServiceImpl chatRoomServiceImpl;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/getChatRoom")
	public String getChatRoom(@RequestParam("senderPhoneNumber") String friendPhoneNumber, Model model,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		Optional<String> chatRoomId = chatRoomServiceImpl.getChatRoomId(loggedInUser.getPhoneNumber(),
				friendPhoneNumber);
		if (chatRoomId.isPresent()) {
			User chatFriendUser = userService.findUserByPhoneNumber(friendPhoneNumber);

			model.addAttribute("chatFriendUser", chatFriendUser);
			model.addAttribute("loggedInUser", loggedInUser);
			return "Chat";
		} else {
			return "showFormHome";
		}
	}

	@MessageMapping("/chat")
	public void processMessage(@Payload ChatMessage chatMessage) {
		ChatMessage savedMsg = chatMessageServiceImpl.save(chatMessage);

	    messagingTemplate.convertAndSendToUser(
	        chatMessage.getReceiverId(), "/queue/messages",
	        new ChatNotification(
	            savedMsg.getId(),
	            savedMsg.getSenderId(),
	            savedMsg.getReceiverId(),
	            savedMsg.getContent()
	        )
	    );
	
	}
	
	

	@GetMapping("/messages/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
			@PathVariable String receiverId) {

		return ResponseEntity.ok(chatMessageServiceImpl.findChatMessages(senderId, receiverId));
	}

}
