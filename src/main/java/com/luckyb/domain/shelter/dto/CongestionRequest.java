package com.luckyb.domain.shelter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongestionRequest {

  private String date; // YYYY-MM-DD, 예측 날짜
  private String time; // HH:mm, 예측 시간
} 