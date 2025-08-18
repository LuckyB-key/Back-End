package com.luckyb.domain.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShelterLikeResponse {

  private String shelterId;
  private int likeCount;
}