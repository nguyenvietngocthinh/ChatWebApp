package com.iuh.ChatWebApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.ChatWebApp.entity.User;



public interface UserRepository extends JpaRepository<User, Integer>{
	boolean existsByPhoneNumber(String phoneNumber);
	
	User findByPhoneNumberAndPassword(String phoneNumber, String password);
	
	User findByPhoneNumber(String phoneNumber);
	
	List<User> findByPhoneNumberContainingIgnoreCase(String phoneNumber);
	
	List<User> findByFullNameContainingIgnoreCase(String fullName);
}
