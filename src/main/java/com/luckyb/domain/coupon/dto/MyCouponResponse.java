package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class MyCouponResponse {
    
    private String userCouponId;
    private String couponId;
    private String title;
    private String description;
    private String status;
    private String issuedAt;
    private String usedAt;
    private String expiryDate;
    private String businessName;
    
    public static MyCouponResponse from(UserCoupon userCoupon) {
        return new MyCouponResponse(
            userCoupon.getUserCouponId(),
            userCoupon.getCoupon().getCouponId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getDescription(),
            userCoupon.getStatus().name(),
            userCoupon.getIssuedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            userCoupon.getUsedAt() != null ? userCoupon.getUsedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
            userCoupon.getCoupon().getExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            userCoupon.getCoupon().getBusinessUser() != null ? userCoupon.getCoupon().getBusinessUser().getNickname() : "알 수 없음"
        );
    }
} 