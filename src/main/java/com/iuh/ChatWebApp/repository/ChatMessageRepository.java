package com.iuh.ChatWebApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.ChatWebApp.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
}