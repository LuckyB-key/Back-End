package com.luckyb.domain.notification.repository;

import com.luckyb.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

  Page<Notification> findByUser_UserId(String userId, Pageable pageable);
  List<Notification> findByUser_UserId(String userId);
}
