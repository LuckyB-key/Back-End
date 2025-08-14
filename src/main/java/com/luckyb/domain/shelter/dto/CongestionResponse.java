package com.luckyb.domain.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CongestionResponse {
    
    private String status;                // "한산함", "보통", "혼잡"
    private Integer currentOccupancy;     // 현재 수용 인원
    private Integer predictedOccupancy;   // 예측 수용 인원
    private Integer capacity;             // 총 수용 가능 인원
    
    public static CongestionResponse of(String status, Integer currentOccupancy, 
                                       Integer predictedOccupancy, Integer capacity) {
        return new CongestionResponse(status, currentOccupancy, predictedOccupancy, capacity);
    }
} 