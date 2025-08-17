package com.luckyb.domain.coupon.entity;

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
@Table(name = "user_coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @Column(name = "user_coupon_id", updatable = false, nullable = false)
    private String userCouponId;

    // 쿠폰을 받은 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 할당된 쿠폰
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserCouponStatus status;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public UserCoupon(User user, Coupon coupon) {
        this.userCouponId = UUID.randomUUID().toString();
        this.user = user;
        this.coupon = coupon;
        this.status = UserCouponStatus.ACTIVE;
        this.issuedAt = LocalDateTime.now();
    }

    // 쿠폰 사용 처리
    public void useCoupon() {
        if (this.status != UserCouponStatus.ACTIVE) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다");
        }
        if (this.coupon.isExpired()) {
            throw new IllegalStateException("만료된 쿠폰입니다");
        }
        
        this.status = UserCouponStatus.USED;
        this.usedAt = LocalDateTime.now();
        // 원본 쿠폰도 사용 처리
        this.coupon.useCoupon(this.user);
    }

    // 사용 가능 여부 확인
    public boolean isUsable() {
        return this.status == UserCouponStatus.ACTIVE && !this.coupon.isExpired();
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return this.coupon.isExpired();
    }

    // 사용자 쿠폰 상태 열거형
    public enum UserCouponStatus {
        ACTIVE("active"),     // 사용 가능
        USED("used"),         // 사용됨
        EXPIRED("expired");   // 만료됨

        private final String value;

        UserCouponStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserCouponStatus fromValue(String value) {
            for (UserCouponStatus status : UserCouponStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown user coupon status: " + value);
        }
    }
} 