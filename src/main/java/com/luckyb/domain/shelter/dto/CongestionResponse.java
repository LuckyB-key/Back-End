package com.luckyb.domain.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CongestionResponse {
    private String status; // "한산함", "보통", "혼잡" 등 AI 예측 결과
    private int currentOccupancy; // 현재 이용 인원
    private int predictedOccupancy; // AI 예측 인원
    private int capacity; // 쉼터 수용 인원
    private String message; // "오전 11시는 비교적 한산합니다"
} 