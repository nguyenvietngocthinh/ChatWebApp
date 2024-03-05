package com.iuh.ChatWebApp.Service;

import com.iuh.ChatWebApp.entity.Friend;
import com.iuh.ChatWebApp.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl {

    @Autowired
    private FriendRepository friendRepository;
    
    public boolean areFriends(String senderPhoneNumber, String receiverPhoneNumber) {
        // Kiểm tra xem đã tồn tại mối quan hệ bạn bè ở cả hai hướng
        boolean exists1 = friendRepository.existsBySenderAndReceiver(senderPhoneNumber, receiverPhoneNumber);
        boolean exists2 = friendRepository.existsBySenderAndReceiver(receiverPhoneNumber, senderPhoneNumber);
        
        // Trả về true nếu đã là bạn bè ở một trong hai hướng, ngược lại trả về false
        return exists1 || exists2;
    }

    public void addFriend(String senderPhoneNumber, String receiverPhoneNumber) {
       
            Friend friend = Friend.builder()
                                 .sender(senderPhoneNumber)
                                 .receiver(receiverPhoneNumber)
                                 .status(false) // Mặc định status là false khi thêm bạn bè
                                 .build();
            friendRepository.save(friend);

    }
}
