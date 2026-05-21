package com.gordeok.chat.repository;

import com.gordeok.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByChatroomId(Long chatroomId);
    List<ChatParticipant> findByUserId(Long userId);
    Optional<ChatParticipant> findByChatroomIdAndUserId(Long chatroomId, Long userId);
    boolean existsByChatroomIdAndUserId(Long chatroomId, Long userId);
    int countByChatroomId(Long chatroomId);
}
