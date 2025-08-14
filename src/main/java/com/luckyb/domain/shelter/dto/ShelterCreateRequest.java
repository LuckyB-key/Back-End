package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShelterCreateRequest {
    
    private String name;
    private AddressDto address;
    private CoordinatesDto coordinates;
    private String type;
    private Integer capacity;
    private List<String> facilities;
    private String operatingHours;
    
    public Shelter toEntity() {
        return Shelter.builder()
                .name(name)
                .address(address != null ? address.toEntity() : null)
                .coordinates(coordinates != null ? coordinates.toEntity() : null)
                .type(Shelter.ShelterType.fromValue(type))
                .capacity(capacity)
                .facilities(facilities)
                .operatingHours(operatingHours)
                .build();
    }
} 