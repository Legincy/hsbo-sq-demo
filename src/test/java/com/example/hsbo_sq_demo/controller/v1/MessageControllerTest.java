package com.example.hsbo_sq_demo.controller.v1;

import com.example.hsbo_sq_demo.model.Message;
import com.example.hsbo_sq_demo.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @Test
    public void testGetAllMessages() throws Exception {
        String content1 = "Neue Test-Nachricht 1";
        Message message1 = new Message(content1);
        message1.setId(1L);

        String content2 = "Neue Test-Nachricht 2";
        Message message2 = new Message(content2);
        message2.setId(2L);

        List<Message> messageList = List.of(message1, message2);

        when(messageService.getAllMessages()).thenReturn(messageList);

        mockMvc.perform(get("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content", is(content1)))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content", is(content2)));
    }

    @Test
    public void testCreateMessage() throws Exception {
        String content = "Neue gespeicherte Nachricht";
        Message message = new Message(content);
        message.setId(1L);

        when(messageService.createMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"" + content + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is(content)));
        }

    @Test
    public void testSearchMessages() throws Exception {
        String searchTerm = "Test";
        String content1 = "Neue Test-Nachricht 1";
        Message message1 = new Message(content1);
        message1.setId(1L);

        String content2 = "Neue Test-Nachricht 2";
        Message message2 = new Message(content2);
        message2.setId(2L);

        List<Message> messageList = List.of(message1, message2);

        when(messageService.getMessagesByContent(searchTerm)).thenReturn(messageList);

        mockMvc.perform(get("/api/v1/messages/search")
                .param("content", searchTerm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content", containsString(searchTerm)))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content", containsString(searchTerm)));

    }
}
