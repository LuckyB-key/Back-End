package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.enums.ShelterType;
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
  private String imageUrl;
  private Integer capacity;
  private List<String> facilities;
  private String operatingHours;
  private String description;

  public ShelterType getShelterType() {
    return type != null ? ShelterType.fromValue(type) : null;
  }
} 