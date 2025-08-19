package com.luckyb.domain.coupon.entity;

import com.luckyb.domain.coupon.enums.CouponStatus;
import com.luckyb.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @Column(name = "coupon_id", updatable = false, nullable = false)
    private String couponId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    // 쿠폰을 발행한 비즈니스 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_user_id", nullable = false)
    private User businessUser;

    // 쿠폰을 사용한 일반 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_by_user_id")
    private User usedByUser;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Coupon(String title, String description, LocalDateTime expiryDate, User businessUser) {
        this.couponId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.expiryDate = expiryDate;
        this.businessUser = businessUser;
        this.status = CouponStatus.ACTIVE;
    }

    // 쿠폰 사용 처리
    public void useCoupon(User user) {
        if (this.status != CouponStatus.ACTIVE) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다");
        }
        if (this.expiryDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("만료된 쿠폰입니다");
        }
        
        this.status = CouponStatus.USED;
        this.usedAt = LocalDateTime.now();
        this.usedByUser = user;
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return this.expiryDate.isBefore(LocalDateTime.now());
    }

    // 사용 가능 여부 확인
    public boolean isUsable() {
        return this.status == CouponStatus.ACTIVE && !isExpired();
    }


} 