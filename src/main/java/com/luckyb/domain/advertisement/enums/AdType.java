package com.luckyb.domain.advertisement.enums;

/**
 * 광고 타입 enum
 */
public enum AdType {

  BANNER("배너 광고"),
  LOCATION_BASED("위치 기반 광고"),
  POPUP("팝업 광고"),
  NOTIFICATION("알림 광고");

  private final String description;

  AdType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
} 