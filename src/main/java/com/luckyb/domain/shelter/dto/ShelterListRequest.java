package com.luckyb.domain.shelter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShelterListRequest {
    
    private Double lat;           // 위도 (필수)
    private Double lng;           // 경도 (필수) 
    private Double distance;      // 검색 반경 (km, 선택)
    private String type;          // 쉼터 유형 (선택)
    private String facilities;    // 쉼표로 구분된 편의시설 (선택)
} 