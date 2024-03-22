package com.iuh.ChatWebApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
	private int id;
    private String senderId;
    private String receiverId;
    private String content;
}
