package com.luckyb.domain.coupon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponListRequest {
    
    private Integer page = 0;      // 기본값 0
    private Integer size = 10;     // 기본값 10
    
    // 유효성 검증
    public int getValidatedPage() {
        return page != null && page >= 0 ? page : 0;
    }
    
    public int getValidatedSize() {
        if (size == null || size <= 0) {
            return 10;
        }
        return Math.min(size, 100); // 최대 100개로 제한
    }
} 