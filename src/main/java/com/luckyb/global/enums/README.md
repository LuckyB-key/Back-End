# Enum 관리 구조

프로젝트의 enum 타입들을 체계적으로 관리하기 위한 구조입니다.

## 🏗️ 구조

```
src/main/java/com/luckyb/
├── global/
│   ├── enums/                    # 공통 enum
│   │   ├── UserRole.java         # 사용자 역할
│   │   └── README.md             # 이 파일
│   └── ai/
│       └── enums/
│           └── AiModelType.java  # AI 모델 타입
├── domain/
│   ├── shelter/
│   │   └── enums/
│   │       ├── ShelterType.java  # 쉼터 타입
│   │       └── ShelterStatus.java # 쉼터 상태
│   ├── coupon/
│   │   └── enums/
│   │       ├── CouponStatus.java # 쿠폰 상태
│   │       └── UserCouponStatus.java # 사용자 쿠폰 상태
│   ├── advertisement/
│   │   └── enums/
│   │       └── AdType.java       # 광고 타입
│   └── announcement/
│       └── enums/
│           └── AnnouncementType.java # 공지사항 타입
```

## 📋 Enum 분류

### 1. Global Enums (공통)

- **UserRole**: 사용자 역할 (USER, BUSINESS, ADMIN)
- **AiModelType**: AI 모델 타입 (GPT_3_5_TURBO, GPT_4, CLAUDE_3)

### 2. Domain Enums (도메인별)

#### Shelter Domain

- **ShelterType**: 쉼터 타입 (PUBLIC, PRIVATE, COMMERCIAL, COMMUNITY)
- **ShelterStatus**: 쉼터 상태 (ACTIVE, INACTIVE, MAINTENANCE, CLOSED)

#### Coupon Domain

- **CouponStatus**: 쿠폰 상태 (ACTIVE, INACTIVE, EXPIRED, DEPLETED)
- **UserCouponStatus**: 사용자 쿠폰 상태 (ISSUED, USED, EXPIRED, CANCELLED)

#### Advertisement Domain

- **AdType**: 광고 타입 (BANNER, LOCATION_BASED, POPUP, NOTIFICATION)

#### Announcement Domain

- **AnnouncementType**: 공지사항 타입 (GENERAL, IMPORTANT, EMERGENCY, MAINTENANCE)

## 🔧 사용법

### Enum 정의

```java
public enum UserRole {
  USER("일반 사용자"),
  BUSINESS("비즈니스 사용자"),
  ADMIN("관리자");

  private final String description;

  UserRole(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static UserRole fromValue(String value) {
    for (UserRole role : UserRole.values()) {
      if (role.name().equals(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Unknown role: " + value);
  }
}
```

### 엔티티에서 사용

```java

@Entity
public class User {

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private UserRole role;

  // import com.luckyb.global.enums.UserRole; 추가 필요
}
```

## 📝 정리 완료 사항

### ✅ 완료된 작업

1. **중복 enum 제거**: 엔티티 내부의 중복된 enum 정의들을 모두 제거
2. **별도 파일 통일**: 모든 enum을 별도 파일로 관리하도록 통일
3. **일관된 구조**: 모든 enum에 동일한 구조 적용 (description + fromValue 메서드)
4. **Import 정리**: 엔티티에서 별도 enum 파일을 import하도록 수정

### 🗂️ 정리된 파일들

- `User.java`: 중복 UserRole enum 제거 → `global.enums.UserRole` 사용
- `Shelter.java`: 중복 ShelterType, ShelterStatus enum 제거 → `shelter.enums.*` 사용
- `Coupon.java`: 중복 CouponStatus enum 제거 → `coupon.enums.CouponStatus` 사용
- `UserCoupon.java`: 중복 UserCouponStatus enum 제거 → `coupon.enums.UserCouponStatus` 사용

### 🔄 변경된 구조

```
이전: 엔티티 내부에 enum 정의
├── User.java
│   └── enum UserRole { ... }
└── Shelter.java
    ├── enum ShelterType { ... }
    └── enum ShelterStatus { ... }

현재: 별도 파일로 enum 관리
├── global/enums/UserRole.java
├── shelter/enums/ShelterType.java
└── shelter/enums/ShelterStatus.java
```

## 🎯 장점

1. **재사용성**: 여러 곳에서 동일한 enum 사용 가능
2. **유지보수성**: enum 수정 시 한 곳에서만 변경
3. **일관성**: 모든 enum이 동일한 구조를 가짐
4. **가독성**: 코드 구조가 더 명확해짐
5. **테스트 용이성**: enum별로 독립적인 테스트 가능 