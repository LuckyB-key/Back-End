package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShelterCreateResponse {
    
    private String id;
    private String name;
    private String status;
    
    public static ShelterCreateResponse from(Shelter shelter) {
        return new ShelterCreateResponse(
            shelter.getShelterId(),
            shelter.getName(),
            shelter.getStatus().getValue()
        );
    }
} 