package com.luckyb.domain.coupon.dto;

import com.luckyb.domain.coupon.entity.Coupon;
import com.luckyb.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {
    
    private String title;
    private String description;
    private String expiryDate; // ISO 8601 형식 문자열
    
    public Coupon toEntity(User businessUser) {
        LocalDateTime parsedExpiryDate = LocalDateTime.parse(
            expiryDate, 
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );
        
        return Coupon.builder()
                .title(title)
                .description(description)
                .expiryDate(parsedExpiryDate)
                .businessUser(businessUser)
                .build();
    }
} 