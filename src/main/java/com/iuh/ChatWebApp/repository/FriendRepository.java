package com.iuh.ChatWebApp.repository;

import com.iuh.ChatWebApp.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
	// Trong FriendRepository
	Friend findBySenderAndReceiver(String senderPhoneNumber, String receiverPhoneNumber);

	boolean existsBySenderAndReceiver(String senderPhoneNumber, String receiverPhoneNumber);

	

}
