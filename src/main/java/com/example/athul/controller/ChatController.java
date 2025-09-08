package com.example.athul.controller;

import com.example.athul.model.ChatMessage;
import com.example.athul.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat") // Frontend sends to /app/chat
    public void sendMessage(ChatMessage message, Principal principal) {
        // 1) Stamp in the real sender
        message.setSenderId(principal.getName());

        // 2) Persist to database
        chatService.save(message);

        // 3) Broadcast to recipient's user-specific queue
        messagingTemplate.convertAndSendToUser(
            message.getReceiverId(),  // must match Principal.getName() of receiver
            "/queue/messages",
            message
        );
    }
}
