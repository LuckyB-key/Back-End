package com.luckyb.domain.shelter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShelterRecommendationRequest {
    
    private Double lat;           // 위도 (필수)
    private Double lng;           // 경도 (필수)
    private Double distance;      // 검색 반경 (km, 선택)
    private String preferences;   // 쉼표로 구분된 선호시설 (선택)
} 