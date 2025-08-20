package com.luckyb.domain.announcement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementCreateRequest {

  private String title;
  private String content;
  private String imageUrl;
  private String author;
} 