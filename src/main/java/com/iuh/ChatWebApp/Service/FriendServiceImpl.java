package com.iuh.ChatWebApp.Service;

import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.entity.User;
import com.iuh.ChatWebApp.repository.FriendRepository;
import com.iuh.ChatWebApp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl {

    @Autowired
    private FriendRepository friendRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public boolean isFriend(String phoneNumber1, String phoneNumber2) {
        // Kiểm tra xem có mối quan hệ bạn bè nào tồn tại giữa hai số điện thoại hay không
        return friendRepository.existsBySenderAndReceiver(phoneNumber1, phoneNumber2) ||
                friendRepository.existsBySenderAndReceiver(phoneNumber2, phoneNumber1);
    }


    public void addFriend(String senderPhoneNumber, String receiverPhoneNumber) {
        // Kiểm tra xem đã tồn tại mối quan hệ bạn bè giữa hai số điện thoại hay chưa
        Friend existingFriendship1 = friendRepository.findBySenderAndReceiver(senderPhoneNumber, receiverPhoneNumber);
        Friend existingFriendship2 = friendRepository.findBySenderAndReceiver(receiverPhoneNumber, senderPhoneNumber);

        if (existingFriendship1 == null && existingFriendship2 == null) {
            // Nếu không tồn tại, thêm mối quan hệ bạn bè mới
            Friend friend = Friend.builder()
                                 .sender(senderPhoneNumber)
                                 .receiver(receiverPhoneNumber)
                                 .status(false)
                                 .build();
            friendRepository.save(friend);
        }
    }
    
    public List<User> getFriendListByPhoneNumber(String phoneNumber) {
        List<Friend> friends = friendRepository.findBySenderOrReceiver(phoneNumber, phoneNumber);
        List<User> friendList = new ArrayList<>();
        for (Friend friend : friends) {
            String friendPhoneNumber = friend.getSender().equals(phoneNumber) ? friend.getReceiver() : friend.getSender();
            // Lấy thông tin người dùng dựa trên số điện thoại của bạn bè
            User friendUser = userRepository.findByPhoneNumber(friendPhoneNumber);
            if (friendUser != null) {
                friendList.add(friendUser);
            }
        }
        return friendList;
    }
}
