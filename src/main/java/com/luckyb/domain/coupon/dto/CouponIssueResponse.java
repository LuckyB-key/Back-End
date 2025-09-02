package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CouponIssueResponse {

  private String userCouponId;
  private String couponId;
  private String title;
  private String description;
  private String status;
  private String issuedAt;
  private String expiryDate;

  public static CouponIssueResponse from(UserCoupon userCoupon) {
    return new CouponIssueResponse(
        userCoupon.getUserCouponId(),
        userCoupon.getCoupon().getCouponId(),
        userCoupon.getCoupon().getTitle(),
        userCoupon.getCoupon().getDescription(),
        userCoupon.getStatus().name(),
        userCoupon.getIssuedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        userCoupon.getCoupon().getExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
} 