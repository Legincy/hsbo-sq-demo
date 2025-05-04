package com.example.hsbo_sq_demo.repository;

import com.example.hsbo_sq_demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // This interface will automatically provide CRUD operations for the Message entity
    // No additional code is needed here
}
