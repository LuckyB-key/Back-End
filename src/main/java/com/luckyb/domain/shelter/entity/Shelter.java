package com.luckyb.domain.shelter.entity;

import com.luckyb.domain.shelter.enums.ShelterStatus;
import com.luckyb.domain.shelter.enums.ShelterType;
import jakarta.persistence.*;
import lombok.*;
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

    @Setter
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


} 