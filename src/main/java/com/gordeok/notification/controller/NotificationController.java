package com.gordeok.notification.controller;

import com.gordeok.notification.dto.NotificationResponseDto;
import com.gordeok.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회 (읽지 않은 / 지난 알림 구분)
    // GET /api/notifications?userId=1
    @GetMapping
    public ResponseEntity<Map<String, List<NotificationResponseDto>>> getNotifications(
            @RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    // 알림 읽음 처리
    // PATCH /api/notifications/{id}/read
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    // 알림 전체 읽음 처리
    // PATCH /api/notifications/read-all?userId=1
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
