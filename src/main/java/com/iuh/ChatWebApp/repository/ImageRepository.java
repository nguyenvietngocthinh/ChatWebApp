package com.iuh.ChatWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.ChatWebApp.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
