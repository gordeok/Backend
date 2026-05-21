package com.gordeok.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_participants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatroomId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String role; // SELLER, BUYER

    private Long lastReadMessageId; // 읽지 않은 메시지 수 계산용

    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    public void updateLastRead(Long messageId) {
        this.lastReadMessageId = messageId;
    }
}
