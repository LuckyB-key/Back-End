package com.luckyb.domain.user.controller;

import com.luckyb.domain.user.dto.UserMeResponse;
import com.luckyb.domain.user.dto.UserMeUpdateRequest;
import com.luckyb.domain.user.service.UserService;
import com.luckyb.global.common.ApiResponse;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 내 정보 조회
     */
    @GetMapping("/me")
    public ApiResponse<UserMeResponse> getUserInfo(
            @RequestHeader("Authorization") String authorization
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserMeResponse response = userService.getUserInfo(userId);
        
        return ApiResponse.success(response);
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/me")
    public ApiResponse<UserMeResponse> updateUserInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserMeUpdateRequest request
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(authorization);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
        
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserMeResponse response = userService.updateUserInfo(userId, request);
        
        log.info("사용자 정보 수정 완료. userId: {}", userId);
        return ApiResponse.success(response);
    }
} 