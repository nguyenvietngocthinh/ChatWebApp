package com.iuh.ChatWebApp.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    
    public ChatMessage saveGroup(ChatMessage chatMessage) {
        var chatIdOptional = chatRoomServiceImpl.getChatGroupRoomId(chatMessage.getChatId(), chatMessage.getSenderId());
        if (chatIdOptional.isPresent()) {
            var chatId = chatIdOptional.get();
            System.out.println(chatId);
            chatMessage.setChatId(chatId);
            repository.save(chatMessage);
            return chatMessage;
        } else {
            // Xử lý khi không tìm thấy chatId
            throw new RuntimeException("ChatId not found"); // Hoặc bạn có thể tạo một Exception riêng phù hợp
        }
    }


    public List<ChatMessage> findChatMessages(String senderId, String receiverId) {
        var chatId = chatRoomServiceImpl.getChatRoomId(senderId, receiverId);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
    
    public List<ChatMessage> findChatGroupMessages(String groupName, String senderId) {
        var chatId = chatRoomServiceImpl.getChatGroupRoomId(groupName, senderId);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
    
    
}
