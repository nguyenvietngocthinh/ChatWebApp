package com.iuh.ChatWebApp.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.ChatWebApp.entity.ChatMessage;
import com.iuh.ChatWebApp.repository.ChatMessageRepository;

@Service
public class ChatMessageServiceImpl {
	@Autowired
    private  ChatMessageRepository repository;
	
	@Autowired
    private ChatRoomServiceImpl chatRoomServiceImpl;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomServiceImpl
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId())
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String receiverId) {
        var chatId = chatRoomServiceImpl.getChatRoomId(senderId, receiverId);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
