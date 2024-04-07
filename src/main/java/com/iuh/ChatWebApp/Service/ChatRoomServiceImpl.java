package com.iuh.ChatWebApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.ChatWebApp.entity.ChatRoom;
import com.iuh.ChatWebApp.repository.ChatRoomRepository;

@Service
public class ChatRoomServiceImpl {

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	public Optional<String> getChatRoomId(String senderId, String receiverId) {
		return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId).map(ChatRoom::getChatId).or(() -> {
			return Optional.empty();
		});
	}

	public String createChatId(String senderId, String receiverId) {
		var chatId = String.format("%s_%s", senderId, receiverId);

		ChatRoom senderRecipient = ChatRoom.builder().chatId(chatId).senderId(senderId).receiverId(receiverId).build();

		ChatRoom receiverSender = ChatRoom.builder().chatId(chatId).senderId(receiverId).receiverId(senderId).build();

		chatRoomRepository.save(senderRecipient);
		chatRoomRepository.save(receiverSender);

		return chatId;
	}
	
	public void createGroupChat(String groupName, String senderId, List<String> selectedFriends) {
	    // Lưu thông tin phòng chat vào cơ sở dữ liệu
	    // Lưu senderId của bạn tạo nhóm
	    chatRoomRepository.save(ChatRoom.builder().chatId(groupName).senderId(senderId).build());

	    // Lưu senderId của mỗi bạn bè đã chọn
	    for (String friendId : selectedFriends) {
	        chatRoomRepository.save(ChatRoom.builder().chatId(groupName).senderId(friendId).build());
	    }
	}
}
