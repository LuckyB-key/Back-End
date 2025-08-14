package com.luckyb.domain.shelter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shelters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shelter {

    @Id
    @Column(name = "shelter_id", updatable = false, nullable = false)
    private String shelterId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ShelterType type;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "shelter_facilities",
        joinColumns = @JoinColumn(name = "shelter_id")
    )
    @Column(name = "facility")
    private List<String> facilities = new ArrayList<>();

    @Column(name = "operating_hours", length = 100)
    private String operatingHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShelterStatus status;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "description", length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Shelter(String name, Address address, Coordinates coordinates, ShelterType type,
                   Integer capacity, List<String> facilities, String operatingHours, String description) {
        this.shelterId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.type = type;
        this.capacity = capacity;
        this.facilities = facilities != null ? facilities : new ArrayList<>();
        this.operatingHours = operatingHours;
        this.status = ShelterStatus.ACTIVE;
        this.description = description;
        this.likeCount = 0;
        this.reviewCount = 0;
    }

    // 쉼터 정보 수정 메서드
    public void updateShelterInfo(String name, Address address, Coordinates coordinates, 
                                 ShelterType type, Integer capacity, List<String> facilities,
                                 String operatingHours, String description) {
        if (name != null) {
            this.name = name;
        }
        if (address != null) {
            this.address = address;
        }
        if (coordinates != null) {
            this.coordinates = coordinates;
        }
        if (type != null) {
            this.type = type;
        }
        if (capacity != null) {
            this.capacity = capacity;
        }
        if (facilities != null) {
            this.facilities = new ArrayList<>(facilities);
        }
        if (operatingHours != null) {
            this.operatingHours = operatingHours;
        }
        if (description != null) {
            this.description = description;
        }
    }

    // 좋아요 수 증가 메서드
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소 메서드
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 리뷰 수 증가 메서드
    public void increaseReviewCount() {
        this.reviewCount++;
    }

    // 상태 변경 메서드
    public void updateStatus(ShelterStatus status) {
        this.status = status;
    }

    // 쉼터 타입 열거형
    public enum ShelterType {
        PUBLIC("public"),           // 공공시설
        PRIVATE("private"),         // 민간시설
        COMMERCIAL("commercial"),   // 상업시설
        COMMUNITY("community");     // 커뮤니티 시설

        private final String value;

        ShelterType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ShelterType fromValue(String value) {
            for (ShelterType type : ShelterType.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown shelter type: " + value);
        }
    }

    // 쉼터 상태 열거형
    public enum ShelterStatus {
        ACTIVE("active"),           // 운영 중
        INACTIVE("inactive"),       // 운영 중지
        MAINTENANCE("maintenance"), // 점검 중
        FULL("full");              // 만석

        private final String value;

        ShelterStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ShelterStatus fromValue(String value) {
            for (ShelterStatus status : ShelterStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown shelter status: " + value);
        }
    }
} 