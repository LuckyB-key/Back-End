package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CouponDetailResponse {
    
    private String id;
    private String title;
    private String description;
    private String expiryDate;
    private String businessId;
    private String businessName;
    
    public static CouponDetailResponse from(Coupon coupon) {
        return new CouponDetailResponse(
            coupon.getCouponId(),
            coupon.getTitle(),
            coupon.getDescription(),
            coupon.getExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            coupon.getBusinessUser().getUserId(),
            coupon.getBusinessUser().getNickname()
        );
    }
} 