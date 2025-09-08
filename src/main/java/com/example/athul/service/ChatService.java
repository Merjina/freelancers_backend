package com.example.athul.service;

import com.example.athul.model.ChatMessage;
import com.example.athul.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository repo;

    public ChatService(ChatMessageRepository repo) {
        this.repo = repo;
    }

    public ChatMessage save(ChatMessage msg) {
        return repo.save(msg);
    }

    /**
     * Returns the chat history between user1 and user2,
     * ordered by timestamp ascending.
     */
    public List<ChatMessage> getHistory(String user1, String user2) {
        return repo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
            user1, user2,   // messages sent by user1 to user2
            user2, user1    // messages sent by user2 to user1
        );
    }
}
