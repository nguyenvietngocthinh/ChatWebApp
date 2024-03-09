package com.iuh.ChatWebApp.repository;



import com.iuh.ChatWebApp.entity.Friend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
	// Trong FriendRepository
	Friend findBySenderAndReceiver(String senderPhoneNumber, String receiverPhoneNumber);

	boolean existsBySenderAndReceiver(String senderPhoneNumber, String receiverPhoneNumber);

	List<Friend> findBySenderOrReceiver(String phoneNumber, String phoneNumber2);

	List<Friend> findBySender(String phoneNumber);

	List<Friend> findByReceiver(String phoneNumber);

	void deleteBySenderAndReceiver(String senderPhoneNumber, String receiverPhoneNumber);

	
	

}
