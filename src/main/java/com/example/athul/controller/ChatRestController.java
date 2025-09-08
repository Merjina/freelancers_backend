package com.example.athul.controller;

import com.example.athul.model.ChatMessage;
import com.example.athul.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;

    @Autowired
    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Fetch the chat history between the currently authenticated user
     * and another user identified by 'withUser' (their username).
     */
    @GetMapping("/history")
    public List<ChatMessage> getHistory(
            @RequestParam("withUser") String withUser,
            Principal principal) {
        String me = principal.getName();
        return chatService.getHistory(me, withUser);
    }
}
