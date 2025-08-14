package com.luckyb.domain.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShelterDeleteResponse {
    
    private String id;
    private String message;
    
    public static ShelterDeleteResponse of(String id, String message) {
        return new ShelterDeleteResponse(id, message);
    }
} 