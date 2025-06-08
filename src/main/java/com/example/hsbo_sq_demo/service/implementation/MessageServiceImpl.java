package com.example.hsbo_sq_demo.service.implementation;

import com.example.hsbo_sq_demo.exception.ResourceNotFoundException;
import com.example.hsbo_sq_demo.model.Message;
import com.example.hsbo_sq_demo.repository.MessageRepository;
import com.example.hsbo_sq_demo.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        Instant now = Instant.now();

        Message result = this.messageRepository.save(message);
        log.debug("Saved record to 'Message' repository ({} ms)", (Instant.now().toEpochMilli() - now.toEpochMilli()));

        return result;
    }

    @Override
    public Message getMessageById(Long id) {
        Instant now = Instant.now();
        Optional<Message> fetchedData = this.messageRepository.findById(id);

        if (fetchedData.isEmpty()) {
            log.error("Message not found with id: {}", id);
            return null;
        }

        Message result = fetchedData.get();

        log.debug("Fetched record in 'Message' repository with id: {} ({} ms)", id, (Instant.now().toEpochMilli() - now.toEpochMilli()));

        return result;
    }

    @Override
    public List<Message> getAllMessages() {
        Instant now = Instant.now();
        List<Message> result = new ArrayList<>();

        result = this.messageRepository.findAll();
        log.debug("Fetched {} records in 'Message' repository ({} ms)", result.size(), (Instant.now().toEpochMilli() - now.toEpochMilli()));

        return result;
    }

    @Override
    public List<Message> getMessagesByContent(String content) {
        Instant now = Instant.now();

        List<Message> result = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            log.debug("No content provided - returning all messages");
            return this.messageRepository.findAll();
        }

        result = this.messageRepository.findAll().stream()
                .filter(message -> message.getContent().contains(content))
                .toList();
        log.debug("Fetched {} record in 'Message' repository containing content '{}' ({} ms)", result.size(), content, (Instant.now().toEpochMilli() - now.toEpochMilli()));

        return result;
    }

    @Override
    public Message updateMessage(Long id, Message message) {
        Instant now = Instant.now();

        Message result = this.messageRepository.findById(id).orElse(null);

        if (message == null || message.getContent() == null || message.getContent().isEmpty()) {
            log.error("Invalid message content for update");
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }

        if (result == null) {
            log.error("Message not found with id: {}", id);
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }

        result.setContent(message.getContent());
        this.messageRepository.save(result);
        log.debug("Updated record in 'Message' repository with id: {} ({} ms)", id, (Instant.now().toEpochMilli() - now.toEpochMilli()));


        return result;
    }

    @Override
    public void deleteMessage(Long id) {
        Instant now = Instant.now();

        Message result = this.messageRepository.findById(id).orElse(null);
        if (result == null) {
            log.error("Message not found with id: {}", id);
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }

        this.messageRepository.delete(result);
        log.debug("Deleted record in 'Message' repository with id: {} ({} ms)", id, (Instant.now().toEpochMilli() - now.toEpochMilli()));
    }
}
