package com.iuh.ChatWebApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chatrooms")
@Getter
@Setter
@Builder
public class ChatRoom {
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	   
	    private String chatId;
	    private String senderId;
	    private String receiverId;
}
