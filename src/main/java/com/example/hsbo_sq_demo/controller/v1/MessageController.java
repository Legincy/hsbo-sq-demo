package com.example.hsbo_sq_demo.controller.v1;

import com.example.hsbo_sq_demo.model.Message;
import com.example.hsbo_sq_demo.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message savedMessage = messageService.createMessage(message);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMessage.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedMessage);
    }

    /**
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("id") Long id) {
        Message message = this.messageService.getMessageById(id);

        return ResponseEntity.ok(message);
    }*/

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = this.messageService.getAllMessages();

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Message>> getMessagesByContent(@RequestParam("content") String content) {
        List<Message> messages = this.messageService.getMessagesByContent(content);

        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable("id") Long id, @RequestBody Message message) {
        Message updatedMessage = this.messageService.updateMessage(id, message);

        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        this.messageService.deleteMessage(id);

        return ResponseEntity.ok(null);
    }
}
