package com.luckyb.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinResponse {

  private String checkinId;
  private String timestamp; // ISO 8601 형식
} 