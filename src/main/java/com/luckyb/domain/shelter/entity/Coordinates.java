package com.luckyb.domain.shelter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coordinates {
    
    @Column(name = "latitude", nullable = false)
    private Double lat;
    
    @Column(name = "longitude", nullable = false)
    private Double lng;
    
    public static Coordinates of(Double lat, Double lng) {
        return new Coordinates(lat, lng);
    }
} 