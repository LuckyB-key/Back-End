package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShelterUpdateRequest {
    
    private String id;
    private String name;
    private AddressDto address;
    private CoordinatesDto coordinates;
    private String type;
    private Integer capacity;
    private List<String> facilities;
    private String operatingHours;
    private String description;
    
    public Shelter.ShelterType getShelterType() {
        return type != null ? Shelter.ShelterType.fromValue(type) : null;
    }
} 