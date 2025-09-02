package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CouponUseResponse {

  private String id;
  private String title;
  private String description;
  private String status;
  private String usedAt;
  private String userId;

  public static CouponUseResponse from(Coupon coupon) {
    return new CouponUseResponse(
        coupon.getCouponId(),
        coupon.getTitle(),
        coupon.getDescription(),
        coupon.getStatus().name(),
        coupon.getUsedAt() != null ? coupon.getUsedAt()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
        coupon.getUsedByUser() != null ? coupon.getUsedByUser().getUserId() : null
    );
  }
} 