package com.gordeok.notification.repository;

import com.gordeok.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 읽지 않은 알림 (최신순) - 오늘 알림 영역
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // 읽은 알림 (최신순) - 지난 알림 영역
    List<Notification> findByUserIdAndIsReadTrueOrderByCreatedAtDesc(Long userId);
}
