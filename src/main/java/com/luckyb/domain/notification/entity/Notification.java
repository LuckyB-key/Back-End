package com.luckyb.domain.notification.entity;

import com.luckyb.domain.notification.enums.NotificationType;
import com.luckyb.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notification")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String shelterId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  private String content;

  @Setter
  @Column(name = "readStatus")
  private boolean readStatus;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
