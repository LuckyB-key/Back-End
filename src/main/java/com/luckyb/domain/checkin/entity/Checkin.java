package com.luckyb.domain.checkin.entity;

import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @Column(name = "checkin_time", nullable = false)
    @CreatedDate
    private LocalDateTime checkinTime;

    @Column(name = "checkout_time")
    private LocalDateTime checkoutTime;

    @Builder
    public Checkin(User user, Shelter shelter) {
        this.user = user;
        this.shelter = shelter;
    }

    public void checkout() {
        this.checkoutTime = LocalDateTime.now();
    }

    public boolean isCheckedOut() {
        return this.checkoutTime != null;
    }
} 