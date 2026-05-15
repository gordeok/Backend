package com.gordeok.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatroomId;

    @Column(nullable = false)
    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String messageType;

    private String visibility;

    private LocalDateTime createdAt;
}
