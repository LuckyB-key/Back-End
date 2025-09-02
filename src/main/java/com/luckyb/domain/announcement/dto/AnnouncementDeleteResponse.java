package com.luckyb.domain.announcement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementDeleteResponse {

  private String message;
  private String announcementId;
}