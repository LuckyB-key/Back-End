package com.luckyb.domain.coupon.service;

import com.luckyb.domain.coupon.dto.*;
import com.luckyb.domain.coupon.entity.Coupon;
import com.luckyb.domain.coupon.entity.UserCoupon;
import com.luckyb.domain.coupon.repository.CouponRepository;
import com.luckyb.domain.coupon.repository.UserCouponRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.repository.UserRepository;
import com.luckyb.global.enums.UserRole;
import com.luckyb.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    /**
     * 쿠폰 등록
     */
    @Transactional
    public CouponCreateResponse createCoupon(String businessUserId, CouponCreateRequest request) {
        // 비즈니스 사용자 조회 및 권한 확인
        User businessUser = userRepository.findById(businessUserId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, businessUserId));
        
        if (businessUser.getRole() != UserRole.BUSINESS) {
            throw new RuntimeException("비즈니스 사용자만 쿠폰을 등록할 수 있습니다");
        }

        try {
            Coupon coupon = request.toEntity(businessUser);
            Coupon savedCoupon = couponRepository.save(coupon);
            
            log.info("새 쿠폰이 등록되었습니다. couponId: {}, businessUserId: {}", 
                     savedCoupon.getCouponId(), businessUserId);
            
            return CouponCreateResponse.from(savedCoupon);
        } catch (Exception e) {
            log.error("쿠폰 생성 실패. businessUserId: {}, error: {}", businessUserId, e.getMessage());
            throw new RuntimeException("쿠폰 생성에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 쿠폰 목록 조회
     */
    public List<CouponListResponse> getCouponList(CouponListRequest request) {
        Pageable pageable = PageRequest.of(
            request.getValidatedPage(), 
            request.getValidatedSize()
        );
        
        Page<Coupon> coupons = couponRepository.findAllActiveCoupons(pageable);
        
        return coupons.getContent().stream()
                .map(CouponListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 쿠폰 상세 조회
     */
    public CouponDetailResponse getCouponDetail(String couponId) {
        Coupon coupon = couponRepository.findActiveCouponById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND, couponId));
        
        return CouponDetailResponse.from(coupon);
    }

    /**
     * 쿠폰 발급
     */
    @Transactional
    public CouponIssueResponse issueCoupon(String couponId, String userId) {
        try {
            // 쿠폰 조회
            Coupon coupon = couponRepository.findActiveCouponById(couponId)
                    .orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND, couponId));
            
            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

            // 이미 발급받은 쿠폰인지 확인
            if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new RuntimeException("이미 발급받은 쿠폰입니다");
            }

            // 쿠폰 만료 여부 확인
            if (coupon.isExpired()) {
                throw new RuntimeException("만료된 쿠폰입니다");
            }

            // 사용자에게 쿠폰 할당
            UserCoupon userCoupon = UserCoupon.builder()
                    .user(user)
                    .coupon(coupon)
                    .build();
            
            UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
            
            log.info("쿠폰이 발급되었습니다. couponId: {}, userId: {}", couponId, userId);
            
            return CouponIssueResponse.from(savedUserCoupon);
        } catch (CouponNotFoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("쿠폰 발급 실패. couponId: {}, userId: {}, error: {}", 
                     couponId, userId, e.getMessage(), e);
            throw new RuntimeException("쿠폰 발급에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 쿠폰 사용 처리
     */
    @Transactional
    public CouponUseResponse useCoupon(String couponId, String userId) {
        // 사용자 쿠폰 조회
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND, couponId));

        // 사용 가능 여부 확인
        if (!userCoupon.isUsable()) {
            if (userCoupon.isExpired()) {
                throw new RuntimeException("만료된 쿠폰입니다");
            } else {
                throw new RuntimeException("이미 사용된 쿠폰입니다");
            }
        }

        try {
            // 쿠폰 사용 처리
            userCoupon.useCoupon();
            UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
            
            log.info("쿠폰이 사용되었습니다. couponId: {}, userId: {}", couponId, userId);
            
            return CouponUseResponse.from(savedUserCoupon.getCoupon());
        } catch (Exception e) {
            log.error("쿠폰 사용 실패. couponId: {}, userId: {}, error: {}", 
                     couponId, userId, e.getMessage());
            throw new RuntimeException("쿠폰 사용에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 만료된 쿠폰 상태 업데이트 (배치 작업용)
     */
    @Transactional
    public void updateExpiredCouponsStatus() {
        List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(LocalDateTime.now());
        
        for (Coupon coupon : expiredCoupons) {
            // 만료된 쿠폰의 상태를 EXPIRED로 변경
            try {
                // 리플렉션이나 직접 상태 변경 메서드를 추가해야 할 수 있음
                log.info("만료된 쿠폰 발견: {}", coupon.getCouponId());
            } catch (Exception e) {
                log.error("만료된 쿠폰 상태 업데이트 실패: {}", coupon.getCouponId());
            }
        }
    }

    /**
     * 비즈니스 사용자의 쿠폰 목록 조회
     */
    public List<CouponListResponse> getBusinessUserCoupons(String businessUserId) {
        User businessUser = userRepository.findById(businessUserId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, businessUserId));
        
        List<Coupon> coupons = couponRepository.findByBusinessUserOrderByCreatedAtDesc(businessUser);
        
        return coupons.stream()
                .map(CouponListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 내 쿠폰 목록 조회
     */
    public List<MyCouponResponse> getMyCoupons(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
        
        List<UserCoupon> userCoupons = userCouponRepository.findActiveUserCouponsByUserId(userId);
        
        return userCoupons.stream()
                .map(MyCouponResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 사용한 쿠폰 목록 조회
     */
    public List<CouponUseResponse> getUserUsedCoupons(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
        
        List<UserCoupon> usedUserCoupons = userCouponRepository.findUsedUserCouponsByUserId(userId);
        
        return usedUserCoupons.stream()
                .map(userCoupon -> CouponUseResponse.from(userCoupon.getCoupon()))
                .collect(Collectors.toList());
    }
} 