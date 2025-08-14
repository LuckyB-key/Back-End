package com.luckyb.domain.user.entity;

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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private String userId;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_preferences",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "preference")
    private List<String> preferences = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String nickname, String email, UserRole role, List<String> preferences) {
        this.userId = UUID.randomUUID().toString();
        this.nickname = nickname;
        this.email = email;
        this.role = role != null ? role : UserRole.USER;
        this.preferences = preferences != null ? preferences : new ArrayList<>();
    }

    // 기본 사용자 생성을 위한 정적 팩토리 메서드
    public static User createDefaultUser() {
        User user = new User();
        user.userId = UUID.randomUUID().toString();
        user.role = UserRole.USER;
        user.preferences = new ArrayList<>();
        return user;
    }

    // 수동 생성자 (디버깅용)
    public User(UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.role = role != null ? role : UserRole.USER;
        this.preferences = new ArrayList<>();
    }

    // 정보 수정 메서드
    public void updateInfo(String nickname, String email, List<String> preferences) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (email != null) {
            this.email = email;
        }
        if (preferences != null) {
            this.preferences = new ArrayList<>(preferences);
        }
    }

    public enum UserRole {
        USER("user"),
        BUSINESS("business"),
        ADMIN("admin");

        private final String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserRole fromValue(String value) {
            for (UserRole role : UserRole.values()) {
                if (role.getValue().equals(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + value);
        }
    }
} 