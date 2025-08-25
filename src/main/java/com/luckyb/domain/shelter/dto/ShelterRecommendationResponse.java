package com.luckyb.domain.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelterRecommendationResponse {

  private String id;
  private String name;
  private double distance;
  private String status;
  private List<String> facilities;
  private String predictedCongestion;
} 