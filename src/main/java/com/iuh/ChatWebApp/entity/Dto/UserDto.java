package com.iuh.ChatWebApp.entity.Dto;

import org.springframework.web.multipart.MultipartFile;

public class UserDto {
	private String phoneNumber;
	private String fullName;
	private String gender;
	private String dob;
	private String password;
	private MultipartFile avatarFile;
	private boolean isOnline;
	private boolean isAdmin;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public MultipartFile getAvatarFile() {
		return avatarFile;
	}
	public void setAvatarFile(MultipartFile avatarFile) {
		this.avatarFile = avatarFile;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
}
