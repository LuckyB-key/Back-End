package com.luckyb.domain.coupon.repository;

import com.luckyb.domain.coupon.entity.Coupon;
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
public interface CouponRepository extends JpaRepository<Coupon, String> {
    
    /**
     * 쿠폰 ID로 활성 상태인 쿠폰 조회
     */
    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId AND c.status != 'CANCELLED'")
    Optional<Coupon> findActiveCouponById(@Param("couponId") String couponId);
    
    /**
     * 모든 활성 쿠폰 페이징 조회
     */
    @Query("SELECT c FROM Coupon c WHERE c.status != 'CANCELLED' ORDER BY c.createdAt DESC")
    Page<Coupon> findAllActiveCoupons(Pageable pageable);
    
    /**
     * 비즈니스 사용자가 발행한 쿠폰 목록 조회
     */
    List<Coupon> findByBusinessUserOrderByCreatedAtDesc(User businessUser);
    
    /**
     * 특정 사용자가 사용한 쿠폰 목록 조회
     */
    List<Coupon> findByUsedByUserOrderByUsedAtDesc(User usedByUser);
    
    /**
     * 상태별 쿠폰 조회
     */
    List<Coupon> findByStatusOrderByCreatedAtDesc(Coupon.CouponStatus status);
    
    /**
     * 만료 예정 쿠폰 조회
     */
    @Query("SELECT c FROM Coupon c WHERE c.status = 'ACTIVE' AND c.expiryDate BETWEEN :now AND :expirySoon")
    List<Coupon> findCouponsExpiringSoon(@Param("now") LocalDateTime now, 
                                        @Param("expirySoon") LocalDateTime expirySoon);
    
    /**
     * 만료된 쿠폰 조회
     */
    @Query("SELECT c FROM Coupon c WHERE c.status = 'ACTIVE' AND c.expiryDate < :now")
    List<Coupon> findExpiredCoupons(@Param("now") LocalDateTime now);
    
    /**
     * 제목으로 쿠폰 검색 (부분 일치)
     */
    @Query("SELECT c FROM Coupon c WHERE c.title LIKE %:title% AND c.status != 'CANCELLED' ORDER BY c.createdAt DESC")
    List<Coupon> findByTitleContaining(@Param("title") String title);
    
    /**
     * 비즈니스 이름으로 쿠폰 검색
     */
    @Query("SELECT c FROM Coupon c JOIN c.businessUser u WHERE u.nickname LIKE %:businessName% AND c.status != 'CANCELLED' ORDER BY c.createdAt DESC")
    List<Coupon> findByBusinessNameContaining(@Param("businessName") String businessName);
} 