package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CouponListResponse {
    
    private String id;
    private String title;
    private String description;
    private String expiryDate;
    private String businessName;
    
    public static CouponListResponse from(Coupon coupon) {
        return new CouponListResponse(
            coupon.getCouponId(),
            coupon.getTitle(),
            coupon.getDescription(),
            coupon.getExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            coupon.getBusinessUser().getNickname()
        );
    }
} 