package com.luckyb.domain.shelter.enums;

/**
 * 쉼터 타입 enum
 */
public enum ShelterType {
  PUBLIC("공공 쉼터"),
  PRIVATE("민간 쉼터"),
  COMMERCIAL("상업 시설"),
  COMMUNITY("지역사회 시설");

  private final String description;

  ShelterType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static ShelterType fromValue(String value) {
    for (ShelterType type : ShelterType.values()) {
      if (type.name().equals(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown shelter type: " + value);
  }
}