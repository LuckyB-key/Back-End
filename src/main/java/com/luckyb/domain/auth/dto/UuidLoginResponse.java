package com.luckyb.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UuidLoginResponse {
    private String uuid;
    private String accessToken;

    public static UuidLoginResponse of(String uuid, String accessToken) {
        return new UuidLoginResponse(uuid, accessToken);
    }
} 