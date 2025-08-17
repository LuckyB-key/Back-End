package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponCreateResponse {
    
    private String id;
    private String title;
    private String businessId;
    
    public static CouponCreateResponse from(Coupon coupon) {
        return new CouponCreateResponse(
            coupon.getCouponId(),
            coupon.getTitle(),
            coupon.getBusinessUser().getUserId()
        );
    }
} 