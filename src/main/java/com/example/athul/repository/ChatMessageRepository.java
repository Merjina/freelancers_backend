package com.example.athul.repository;

import com.example.athul.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Fetches all chat messages between two users (in both directions),
     * ordered by timestamp ascending.
     *
     * @param senderId1   the first user's ID (or username)
     * @param receiverId1 the second user's ID (or username)
     * @param senderId2   the second user's ID again
     * @param receiverId2 the first user's ID again
     * @return list of messages exchanged between the two users
     */
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
        String senderId1, String receiverId1,
        String senderId2, String receiverId2
    );
}
