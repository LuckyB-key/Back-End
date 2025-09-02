package com.luckyb.domain.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdListResponse {

  /**
   * 광고 ID
   */
  private String id;

  /**
   * 광고 타입 (banner, location_based)
   */
  private String adType;

  /**
   * 광고 내용
   */
  private String content;

  /**
   * 광고주 이름
   */
  private String businessName;

  /**
   * 이미지 URL
   */
  private String image;
} 