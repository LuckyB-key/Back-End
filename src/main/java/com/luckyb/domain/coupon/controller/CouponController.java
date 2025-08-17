package com.luckyb.domain.coupon.controller;

import com.luckyb.domain.coupon.dto.*;
import com.luckyb.domain.coupon.service.CouponService;
import com.luckyb.global.common.ApiResponse;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 쿠폰 등록
     */
    @PostMapping
    public ApiResponse<CouponCreateResponse> createCoupon(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CouponCreateRequest request
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        CouponCreateResponse response = couponService.createCoupon(userId, request);
        
        log.info("새 쿠폰이 등록되었습니다. couponId: {}, businessUserId: {}", 
                 response.getId(), userId);
        return ApiResponse.success(response);
    }

    /**
     * 쿠폰 목록 조회
     */
    @GetMapping
    public ApiResponse<List<CouponListResponse>> getCouponList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        CouponListRequest request = new CouponListRequest();
        request.setPage(page);
        request.setSize(size);
        
        List<CouponListResponse> coupons = couponService.getCouponList(request);
        return ApiResponse.success(coupons);
    }

    /**
     * 내가 발행한 쿠폰 목록 조회 (비즈니스 사용자용)
     */
    @GetMapping("/my-business")
    public ApiResponse<List<CouponListResponse>> getMyBusinessCoupons(
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        List<CouponListResponse> coupons = couponService.getBusinessUserCoupons(userId);
        
        return ApiResponse.success(coupons);
    }

    /**
     * 내 쿠폰 목록 조회
     */
    @GetMapping("/my-coupons")
    public ApiResponse<List<MyCouponResponse>> getMyCoupons(
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        List<MyCouponResponse> myCoupons = couponService.getMyCoupons(userId);
        
        return ApiResponse.success(myCoupons);
    }

    /**
     * 내가 사용한 쿠폰 목록 조회
     */
    @GetMapping("/my-used")
    public ApiResponse<List<CouponUseResponse>> getMyUsedCoupons(
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        List<CouponUseResponse> usedCoupons = couponService.getUserUsedCoupons(userId);
        
        return ApiResponse.success(usedCoupons);
    }

    /**
     * 쿠폰 상세 조회
     */
    @GetMapping("/{couponId}")
    public ApiResponse<CouponDetailResponse> getCouponDetail(
            @PathVariable String couponId
    ) {
        CouponDetailResponse coupon = couponService.getCouponDetail(couponId);
        return ApiResponse.success(coupon);
    }

    /**
     * 쿠폰 발급
     */
    @PostMapping("/{couponId}/issue")
    public ApiResponse<CouponIssueResponse> issueCoupon(
            @PathVariable String couponId,
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        CouponIssueResponse response = couponService.issueCoupon(couponId, userId);
        
        log.info("쿠폰이 발급되었습니다. couponId: {}, userId: {}", couponId, userId);
        return ApiResponse.success(response);
    }

    /**
     * 쿠폰 사용 처리
     */
    @PostMapping("/{couponId}/use")
    public ApiResponse<CouponUseResponse> useCoupon(
            @PathVariable String couponId,
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        CouponUseResponse response = couponService.useCoupon(couponId, userId);
        
        log.info("쿠폰이 사용되었습니다. couponId: {}, userId: {}", couponId, userId);
        return ApiResponse.success(response);
    }
} 