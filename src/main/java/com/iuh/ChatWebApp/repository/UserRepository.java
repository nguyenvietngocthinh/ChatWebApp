package com.iuh.ChatWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.ChatWebApp.entity.User;



public interface UserRepository extends JpaRepository<User, Integer>{
	boolean existsByPhoneNumber(String phoneNumber);
	
	User findByPhoneNumberAndPassword(String phoneNumber, String password);
	
	User findByPhoneNumber(String phoneNumber);
}
