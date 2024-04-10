package com.iuh.ChatWebApp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iuh.ChatWebApp.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatId(String chatId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.timestamp = :timestamp")
    List<ChatMessage> findByTimestamp(@Param("timestamp") Date timestamp);
}