package com.luckyb.domain.notification.dto;

import com.luckyb.domain.notification.entity.Notification;
import com.luckyb.domain.notification.enums.NotificationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  private String id;
  private String shelterId;
  private NotificationType type;
  private String content;
  private boolean readStatus;
  private LocalDateTime createdAt;

  public static NotificationResponse from(Notification notification) {
    return NotificationResponse.builder()
        .id(notification.getId())
        .shelterId(notification.getShelterId())
        .type(notification.getType())
        .content(notification.getContent())
        .readStatus(notification.isReadStatus())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}
