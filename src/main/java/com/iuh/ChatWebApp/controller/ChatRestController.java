package com.iuh.ChatWebApp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.ChatWebApp.Service.ChatMessageServiceImpl;
import com.iuh.ChatWebApp.Service.ChatRoomServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.ChatMessage;
import com.iuh.ChatWebApp.entity.ChatNotification;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
	@Autowired
	private ChatMessageServiceImpl chatMessageServiceImpl;

	@Autowired
	private ChatRoomServiceImpl chatRoomServiceImpl;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/getChatRoomMobile")
	public ResponseEntity<?> getChatRoomMobile(@RequestParam("senderPhoneNumber") String friendPhoneNumber,
			HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
		}
		Optional<String> chatRoomId = chatRoomServiceImpl.getChatRoomId(loggedInUser.getPhoneNumber(),
				friendPhoneNumber);
		if (chatRoomId.isPresent()) {
			User chatFriendUser = userService.findUserByPhoneNumber(friendPhoneNumber);
			// Đảm bảo trả về dữ liệu mà frontend có thể sử dụng, ví dụ JSON
			Map<String, Object> response = new HashMap<>();
			response.put("chatFriendUser", chatFriendUser);
			response.put("loggedInUser", loggedInUser);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@MessageMapping("/chatM")
	public void processMessageM(@Payload ChatMessage chatMessage) {
		ChatMessage savedMsg = chatMessageServiceImpl.save(chatMessage);

		messagingTemplate.convertAndSendToUser(chatMessage.getReceiverId(), "/queue/messages", new ChatNotification(
				savedMsg.getId(), savedMsg.getSenderId(), savedMsg.getReceiverId(), savedMsg.getContent()));

	}

	@GetMapping("/messagesM/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChatMessagesM(@PathVariable String senderId,
			@PathVariable String receiverId) {

		return ResponseEntity.ok(chatMessageServiceImpl.findChatMessages(senderId, receiverId));
	}

	@DeleteMapping("/messages/{timestamp}")
	public ResponseEntity<?> deleteChatMessageByTimestamp(@PathVariable long timestamp) { // Sửa đổi tham số để là timestamp
	    // Kiểm tra và xóa tin nhắn dựa trên timestamp
		messagingTemplate.convertAndSend("/topic/deleteMessage", "{\"timestamp\":" + timestamp + "}");
	    boolean messageDeleted = chatMessageServiceImpl.deleteChatMessageByTimestamp(timestamp);
	    if (messageDeleted) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}
