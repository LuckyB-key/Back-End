package com.luckyb.domain.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdListRequest {
    
    /**
     * 위도 (필수)
     */
    private Double lat;
    
    /**
     * 경도 (필수)
     */
    private Double lng;
    
    /**
     * 사용자 ID (개인화된 광고를 위해 사용, 선택)
     */
    private String userId;
} 