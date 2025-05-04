package com.example.hsbo_sq_demo.service;

import com.example.hsbo_sq_demo.model.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(Message message);
    Message getMessageById(Long id);
    List<Message> getAllMessages();
    List<Message> getMessagesByContent(String content);
    Message updateMessage(Long id, Message message);
    void deleteMessage(Long id);
}
