package com.gordeok.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    // 알림 타입: "RESERVED"(예약완료), "DEADLINE"(마감임박)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    // 읽음 여부
    @Column(nullable = false)
    private Boolean isRead = false;

    private LocalDateTime createdAt;

    public Notification(Long userId, String type, String message) {
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
