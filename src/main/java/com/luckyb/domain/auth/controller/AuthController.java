package com.luckyb.domain.auth.controller;

import com.luckyb.domain.auth.dto.UuidLoginResponse;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.service.UserService;
import com.luckyb.global.common.ApiResponse;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * UUID 최초 생성 및 로그인
     */
    @PostMapping("/uuid")
    public ApiResponse<UuidLoginResponse> createUuidAndLogin() {
        try {
            // 새 사용자 생성
            User user = userService.createUser();
            
            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateToken(user.getUserId());
            
            // 응답 생성
            UuidLoginResponse response = UuidLoginResponse.of(user.getUserId(), accessToken);
            
            log.info("새 사용자 UUID 생성 및 로그인 완료. userId: {}", user.getUserId());
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            log.error("UUID 생성 및 로그인 중 오류 발생: {}", e.getMessage(), e);
            return ApiResponse.error("UUID 생성에 실패했습니다: " + e.getMessage());
        }
    }
} 