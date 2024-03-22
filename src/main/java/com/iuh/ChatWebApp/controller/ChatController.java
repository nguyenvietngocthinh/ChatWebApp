package com.iuh.ChatWebApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iuh.ChatWebApp.Service.ChatMessageServiceImpl;
import com.iuh.ChatWebApp.entity.ChatMessage;
import com.iuh.ChatWebApp.entity.ChatNotification;

@Controller
public class ChatController {
	  @Autowired
	  private ChatMessageServiceImpl chatMessageServiceImpl;
	  
	  @Autowired
	  private SimpMessagingTemplate messagingTemplate;

	    @MessageMapping("/chat")
	    public void processMessage(@Payload ChatMessage chatMessage) {
	        ChatMessage savedMsg = chatMessageServiceImpl.save(chatMessage);
	        messagingTemplate.convertAndSendToUser(
	                chatMessage.getReceivertId(), "/queue/messages",
	                new ChatNotification(
	                        savedMsg.getId(),
	                        savedMsg.getSenderId(),
	                        savedMsg.getReceivertId(),
	                        savedMsg.getContent()
	                )
	        );
	    }

	    @GetMapping("/messages/{senderId}/{recipientId}")
	    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
	                                                 @PathVariable String recipientId) {
	        return ResponseEntity
	                .ok(chatMessageServiceImpl.findChatMessages(senderId, recipientId));
	    }

}
