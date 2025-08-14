package com.luckyb.domain.user.dto;

import com.luckyb.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserMeResponse {
    private String userId;
    private String nickname;
    private String email;
    private String role;
    private List<String> preferences;

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
            user.getUserId(),
            user.getNickname(),
            user.getEmail(),
            user.getRole().getValue(),
            user.getPreferences()
        );
    }
} 