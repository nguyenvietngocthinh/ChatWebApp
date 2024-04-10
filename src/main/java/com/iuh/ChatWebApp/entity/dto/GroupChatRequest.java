package com.iuh.ChatWebApp.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupChatRequest {
	private String groupName;
	private List<String> selectedFriends;
	
	
}
