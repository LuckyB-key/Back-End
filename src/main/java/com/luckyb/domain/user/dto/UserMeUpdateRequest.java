package com.luckyb.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserMeUpdateRequest {
    private String nickname;
    private String email;
    private String role; // 역할 변경 필드 추가
    private List<String> preferences;
} 