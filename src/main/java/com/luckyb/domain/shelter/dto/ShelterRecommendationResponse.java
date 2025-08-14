package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShelterRecommendationResponse {
    
    private String id;
    private String name;
    private Double distance;
    private String status;
    private List<String> facilities;
    private String predictedCongestion;
    
    public static ShelterRecommendationResponse from(Shelter shelter, Double distance, String predictedCongestion) {
        return new ShelterRecommendationResponse(
            shelter.getShelterId(),
            shelter.getName(),
            distance,
            shelter.getStatus().getValue(),
            shelter.getFacilities(),
            predictedCongestion
        );
    }
} 