package com.gordeok.chat.repository;

import com.gordeok.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatroomIdOrderByCreatedAtAsc(Long chatroomId);
    Optional<Message> findTopByChatroomIdOrderByCreatedAtDesc(Long chatroomId);
    int countByChatroomIdAndIdGreaterThan(Long chatroomId, Long messageId);
}
