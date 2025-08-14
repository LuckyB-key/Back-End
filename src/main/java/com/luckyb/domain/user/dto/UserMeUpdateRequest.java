package com.luckyb.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserMeUpdateRequest {
    private String nickname;
    private String email;
    private List<String> preferences;
} 