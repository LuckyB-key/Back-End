package com.luckyb.domain.coupon.repository;

import com.luckyb.domain.coupon.entity.UserCoupon;
import com.luckyb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, String> {
    
    /**
     * 사용자 ID로 활성 상태인 사용자 쿠폰 조회
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.userId = :userId AND uc.status = 'ACTIVE' ORDER BY uc.issuedAt DESC")
    List<UserCoupon> findActiveUserCouponsByUserId(@Param("userId") String userId);
    
    /**
     * 사용자 ID로 모든 사용자 쿠폰 조회 (페이징)
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.userId = :userId ORDER BY uc.issuedAt DESC")
    Page<UserCoupon> findUserCouponsByUserId(@Param("userId") String userId, Pageable pageable);
    
    /**
     * 사용자 ID와 쿠폰 ID로 특정 사용자 쿠폰 조회
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.userId = :userId AND uc.coupon.couponId = :couponId")
    Optional<UserCoupon> findByUserIdAndCouponId(@Param("userId") String userId, @Param("couponId") String couponId);
    
    /**
     * 사용자가 사용한 쿠폰 목록 조회
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.userId = :userId AND uc.status = 'USED' ORDER BY uc.usedAt DESC")
    List<UserCoupon> findUsedUserCouponsByUserId(@Param("userId") String userId);
    
    /**
     * 만료된 사용자 쿠폰 조회
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.status = 'ACTIVE' AND uc.coupon.expiryDate < :now")
    List<UserCoupon> findExpiredUserCoupons(@Param("now") LocalDateTime now);
    
    /**
     * 쿠폰 ID로 해당 쿠폰을 받은 모든 사용자 조회
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.coupon.couponId = :couponId")
    List<UserCoupon> findByCouponId(@Param("couponId") String couponId);
    
    /**
     * 사용자가 특정 쿠폰을 이미 받았는지 확인
     */
    @Query("SELECT COUNT(uc) > 0 FROM UserCoupon uc WHERE uc.user.userId = :userId AND uc.coupon.couponId = :couponId")
    boolean existsByUserIdAndCouponId(@Param("userId") String userId, @Param("couponId") String couponId);
} 