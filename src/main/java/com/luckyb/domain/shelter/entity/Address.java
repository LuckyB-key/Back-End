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
public class Address {
    
    @Column(name = "jibun_address", length = 255)
    private String jibun;
    
    @Column(name = "road_address", length = 255)
    private String road;
    
    public static Address of(String jibun, String road) {
        return new Address(jibun, road);
    }
} 