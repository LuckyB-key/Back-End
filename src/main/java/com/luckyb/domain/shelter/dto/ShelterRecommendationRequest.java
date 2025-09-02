package com.luckyb.domain.shelter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShelterRecommendationRequest {

  private double lat; // 위도
  private double lng; // 경도
  private List<String> preferences; // 선호 시설 목록
  private String category; // 추천 카테고리
} 