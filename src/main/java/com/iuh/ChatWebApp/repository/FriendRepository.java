package com.iuh.ChatWebApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.ChatWebApp.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsBySenderAndReceiver(String sender, String receiver);
    List<Friend> findBySenderAndReceiver(String sender, String receiver);
}

