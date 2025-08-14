package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDto {
    
    private Double lat;
    private Double lng;
    
    public static CoordinatesDto from(Coordinates coordinates) {
        if (coordinates == null) {
            return null;
        }
        return new CoordinatesDto(coordinates.getLat(), coordinates.getLng());
    }
    
    public Coordinates toEntity() {
        return Coordinates.of(lat, lng);
    }
} 