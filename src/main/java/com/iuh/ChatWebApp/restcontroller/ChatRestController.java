package com.iuh.ChatWebApp.restcontroller;

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
import com.iuh.ChatWebApp.entity.ChatRoom;
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

	

	@GetMapping("/messagesM/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChatMessagesM(@PathVariable String senderId,
			@PathVariable String receiverId) {

		return ResponseEntity.ok(chatMessageServiceImpl.findChatMessages(senderId, receiverId));
	}
	
	@GetMapping("/getChatGroupRoomM")
    public ResponseEntity<?> getChatGroupRoomM(@RequestParam("groupChatName") String groupChatName,
                                              HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String loggedInUserPhoneNumber = loggedInUser.getPhoneNumber();

        Optional<String> chatGroupRoomId = chatRoomServiceImpl.getChatGroupRoomId(groupChatName, loggedInUserPhoneNumber);

        if (chatGroupRoomId.isPresent()) {
            ChatRoom chatRoomGroup = chatRoomServiceImpl.findByChatIdAndSenderId("Group_" + groupChatName, loggedInUserPhoneNumber);
            return ResponseEntity.ok().body(chatRoomGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/getGroupListChatRoomsM")
	public ResponseEntity<List<ChatRoom>> getGroupListChatRooms(HttpSession session) {
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	    List<ChatRoom> groupChatRooms = chatRoomServiceImpl.getGroupChatRooms(loggedInUser.getPhoneNumber());
	    return ResponseEntity.ok(groupChatRooms);
	}


}
