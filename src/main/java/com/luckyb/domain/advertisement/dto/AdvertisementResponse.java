package com.luckyb.domain.advertisement.dto;

import com.luckyb.domain.advertisement.entity.Advertisement;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponse {

  private Long id;
  private String userId;
  private String title;
  private String content;
  private String image;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static AdvertisementResponse from(Advertisement advertisement) {
    return AdvertisementResponse.builder()
        .id(advertisement.getId())
        .userId(advertisement.getUser().getUserId())
        .title(advertisement.getTitle())
        .content(advertisement.getContent())
        .image(advertisement.getImage())
        .createdAt(advertisement.getCreatedAt())
        .updatedAt(advertisement.getUpdatedAt())
        .build();
  }
}
