package com.iuh.ChatWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.ChatWebApp.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>{

}
