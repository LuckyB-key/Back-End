package com.luckyb.domain.announcement.enums;

/**
 * 공지사항 타입 enum
 */
public enum AnnouncementType {
  GENERAL("일반 공지"),
  IMPORTANT("중요 공지"),
  EMERGENCY("긴급 공지"),
  MAINTENANCE("점검 공지");

  private final String description;

  AnnouncementType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
} 