package com.luckyb.domain.announcement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "announcements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement {

  @Id
  @Column(name = "announcement_id", updatable = false, nullable = false)
  private String announcementId;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "author", nullable = false, length = 100)
  private String author;

  private String imageUrl;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public Announcement(String title, String content, String imageUrl, String author) {
    this.announcementId = UUID.randomUUID().toString();
    this.title = title;
    this.content = content;
    this.imageUrl = imageUrl;
    this.author = author;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // 공지사항 정보 수정 메서드
  public void updateAnnouncement(String title, String content, String imageUrl) {
    if (title != null) {
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
    if (imageUrl != null) {
      this.imageUrl = imageUrl;
    }
    if (updatedAt != null) {
        this.updatedAt = LocalDateTime.now();
    }
  }
} 