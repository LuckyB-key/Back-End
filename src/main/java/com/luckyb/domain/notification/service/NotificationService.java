package com.luckyb.domain.notification.service;

import com.luckyb.domain.notification.entity.Notification;
import com.luckyb.domain.notification.enums.NotificationType;
import com.luckyb.domain.notification.repository.NotificationRepository;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.shelter.repository.ShelterRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.service.UserService;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final ShelterRepository shelterRepository;
  private final UserService userService;


  @Async
  public void createNotification(String userId, String shelterId, NotificationType type,
      String content) {
    User user = userService.getUserById(userId);

    if (!user.isAlarmEnabled()) {
      return;
    }

    Notification notification = Notification.builder()
        .user(user)
        .shelterId(shelterId)
        .type(type)
        .content(content)
        .build();

    notificationRepository.save(notification);
  }

  public Page<Notification> getRecentNotifications(String userId, Pageable pageable) {
    Page<Notification> notifications = notificationRepository.findByUser_UserId(userId, pageable);
    notifications.forEach(notification -> {
      if (!notification.isReadStatus()) {
        notification.setReadStatus(true);
      }
    });

    return notificationRepository.findByUser_UserId(userId, pageable);
  }

  @Transactional
  public void readNotification(String userId, String notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

    if (!notification.getUser().getUserId().equals(userId)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    notification.setReadStatus(true);
    notificationRepository.save(notification);
  }

  @Transactional
  public void readAllNotifications(String userId) {
    List<Notification> notifications = notificationRepository.findByUser_UserId(userId);

    for (Notification notification : notifications) {
      notification.setReadStatus(true);
    }

    notificationRepository.saveAll(notifications);
  }

  public void shelterReviewNotification(String shelterId, String reviewerNickname) {
    Shelter shelter = shelterRepository.findById(shelterId)
        .orElseThrow(() -> new RuntimeException("쉼터를 찾을 수 없습니다: " + shelterId));

    createNotification(
        shelter.getUser().getUserId(),
        shelterId,
        NotificationType.REVIEW,
        reviewerNickname + "님이 당신의 쉼터에 리뷰를 남겼습니다."
    );
  }

  // 쿠폰 발급 알림
  public void couponNotification(String userId, String couponName) {
    createNotification(
        userId,
        null,
        NotificationType.COUPON,
        couponName + " 쿠폰이 발급되었습니다!"
    );
  }

  // 공지사항 등록 알림 (모든 사용자)
  public void announcementNotification(String announcementTitle, List<User> allUsers) {
    for (User user : allUsers) {
      createNotification(
          user.getUserId(),
          null,
          NotificationType.ANNOUNCEMENT,
          "새로운 공지사항이 등록되었습니다: " + announcementTitle
      );
    }
  }

  // 내 쉼터에 좋아요가 달렸을 때
  public void shelterLikeNotification(String shelterId, String likerNickname) {
    Shelter shelter = shelterRepository.findById(shelterId)
        .orElseThrow(() -> new RuntimeException("쉼터를 찾을 수 없습니다: " + shelterId));

    if (likerNickname == null || likerNickname.isBlank()) {
      likerNickname = "익명";
    }

    createNotification(
        shelter.getUser().getUserId(),
        shelterId,
        NotificationType.LIKE,
        likerNickname + "님이 당신의 쉼터를 좋아요를 남겼습니다."
    );
  }

}
