package com.gordeok.notification.service;

import com.gordeok.notification.dto.NotificationResponseDto;
import com.gordeok.notification.entity.Notification;
import com.gordeok.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 목록 조회 (읽지 않은 알림 / 지난 알림 구분)
    public Map<String, List<NotificationResponseDto>> getNotifications(Long userId) {
        List<NotificationResponseDto> unread = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());

        List<NotificationResponseDto> read = notificationRepository
                .findByUserIdAndIsReadTrueOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());

        return Map.of(
                "unread", unread,   // 읽지 않은 알림
                "past", read        // 지난 알림
        );
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        notification.markAsRead();
    }

    // 알림 전체 읽음 처리
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .forEach(Notification::markAsRead);
    }

    // 알림 생성 (다른 서비스에서 호출)
    public void createNotification(Long userId, String type, String message) {
        notificationRepository.save(new Notification(userId, type, message));
    }
}
