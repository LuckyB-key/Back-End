package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShelterUpdateResponse {
    
    private String id;
    private String name;
    private AddressDto address;
    private CoordinatesDto coordinates;
    private String type;
    private Integer capacity;
    private List<String> facilities;
    private String operatingHours;
    private String description;
    
    public static ShelterUpdateResponse from(Shelter shelter) {
        return new ShelterUpdateResponse(
            shelter.getShelterId(),
            shelter.getName(),
            AddressDto.from(shelter.getAddress()),
            CoordinatesDto.from(shelter.getCoordinates()),
            shelter.getType().getValue(),
            shelter.getCapacity(),
            shelter.getFacilities(),
            shelter.getOperatingHours(),
            shelter.getDescription()
        );
    }
} 