package com.example.hsbo_sq_demo.service.implementation;

import com.example.hsbo_sq_demo.exception.ResourceNotFoundException;
import com.example.hsbo_sq_demo.model.Message;
import com.example.hsbo_sq_demo.repository.MessageRepository;
import com.example.hsbo_sq_demo.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        return this.messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long id) {
        return this.messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    @Override
    public List<Message> getAllMessages() {
        return this.messageRepository.findAll();
    }

    @Override
    public List<Message> getMessagesByContent(String content) {
        return this.messageRepository.findAll().stream()
                .filter(message -> message.getContent().contains(content))
                .toList();
    }

    @Override
    public Message updateMessage(Long id, Message message) {
        return this.messageRepository.findById(id)
                .map(existingMessage -> {
                    existingMessage.setContent(message.getContent());
                    existingMessage.setUpdatedAt(Instant.now());
                    return this.messageRepository.save(existingMessage);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    @Override
    public void deleteMessage(Long id) {
        Message message = this.messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        this.messageRepository.delete(message);
    }
}
