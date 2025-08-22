package com.luckyb.domain.announcement.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnnouncementDetailResponse {

  private String announcementId;
  private String title;
  private String content;
  private String imageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
} 