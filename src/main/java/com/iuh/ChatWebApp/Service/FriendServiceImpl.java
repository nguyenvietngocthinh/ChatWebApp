package com.iuh.ChatWebApp.Service; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.repository.FriendRepository;

import java.util.List;

@Service
public class FriendServiceImpl {

	 @Autowired
	    private FriendRepository friendRepository;

	    // Kiểm tra sự tồn tại của mối quan hệ
	    public boolean existsFriendRelation(String sender, String receiver) {
	        return friendRepository.existsBySenderAndReceiver(sender, receiver);
	    }

	    // Lưu mối quan hệ bạn bè mới
	    public void saveFriendRelation(Friend friend) {
	        friendRepository.save(friend);
	    }

	    // Lấy danh sách bạn bè dựa trên sender và receiver
	    public List<Friend> getFriendList(String sender, String receiver) {
	        return friendRepository.findBySenderAndReceiver(sender, receiver);
	    }

	    // Xóa mối quan hệ bạn bè
	    public void deleteFriendRelation(Friend friend) {
	        friendRepository.delete(friend);
	    }

}
