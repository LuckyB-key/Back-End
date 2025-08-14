package com.luckyb.domain.shelter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CongestionRequest {
    
    private String date;    // YYYY-MM-DD, 예측 날짜 (선택)
    private String time;    // HH:mm, 예측 시간 (선택)
} 