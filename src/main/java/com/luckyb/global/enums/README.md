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
}
```

### JPA에서 사용
```java
@Entity
public class User {
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
```

### Controller에서 사용
```java
@GetMapping("/users")
public List<User> getUsersByRole(@RequestParam UserRole role) {
    return userService.findByRole(role);
}
```

## 📝 네이밍 컨벤션

### Enum 클래스명
- **PascalCase** 사용
- **명사 + Type/Status/Role** 형태
- 예: `UserRole`, `ShelterType`, `CouponStatus`

### Enum 상수명
- **UPPER_SNAKE_CASE** 사용
- **명확하고 구체적인 이름** 사용
- 예: `USER`, `BUSINESS_USER`, `ACTIVE`, `INACTIVE`

### 패키지 구조
- **공통 enum**: `global.enums`
- **도메인별 enum**: `domain.{도메인명}.enums`
- **AI 관련 enum**: `global.ai.enums`

## 🔄 마이그레이션 가이드

### 기존 코드에서 새로운 enum 사용하기

1. **Import 추가**
```java
import com.luckyb.global.enums.UserRole;
import com.luckyb.domain.shelter.enums.ShelterType;
```

2. **기존 enum 참조 변경**
```java
// 기존
private User.UserRole role;

// 변경 후
private UserRole role;
```

3. **JPA 엔티티 업데이트**
```java
@Enumerated(EnumType.STRING)
private UserRole role;
```

## 🚀 확장 계획

### 새로운 Enum 추가 시
1. 적절한 패키지 선택 (global vs domain)
2. 명확한 네이밍 적용
3. description 필드 추가
4. 문서화 업데이트

### Enum 유틸리티 메서드
- `fromString()`: 문자열로부터 enum 변환
- `getAllValues()`: 모든 enum 값 반환
- `isValid()`: 유효성 검증 