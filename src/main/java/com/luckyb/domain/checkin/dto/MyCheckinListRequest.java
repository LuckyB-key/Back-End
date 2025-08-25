package com.luckyb.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCheckinListRequest {

  private Integer page;
  private Integer size;
} 