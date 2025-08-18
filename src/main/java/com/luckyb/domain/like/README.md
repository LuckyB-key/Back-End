# 좋아요 도메인 (Like Domain)

사용자의 쉼터 좋아요 기능을 관리하는 모듈입니다.

## 🎯 주요 기능

- **좋아요 관리**: 쉼터에 대한 사용자 좋아요 기능
- **사용자-쉼터 관계**: 다대다 관계 관리
- **좋아요 통계**: 쉼터별 좋아요 수 집계

## 🏗️ 구조

```
domain/like/
├── entity/
│   └── Like.java                    # 좋아요 엔티티
├── repository/
│   └── LikeRepository.java          # 좋아요 데이터 접근
└── README.md                        # 이 파일
```

## 📋 데이터 모델

### Like 엔티티

```java
@Entity
@Table(name = "likes")
public class Like {
    private Long id;
    private Shelter shelter;    // 쉼터
    private User user;          // 사용자
}
```

### 관계 구조

- **User** ↔ **Like** ↔ **Shelter**
- 사용자와 쉼터 간의 다대다 관계
- Like 엔티티가 중간 테이블 역할

## 🔧 사용 예시

### 좋아요 조회

```java
// 사용자가 좋아요한 쉼터 목록 조회
List<Like> userLikes = likeRepository.findByUserId(userId);

// 쉼터의 좋아요 수 조회
long likeCount = likeRepository.countByShelterId(shelterId);

// 특정 사용자가 특정 쉼터를 좋아요했는지 확인
boolean isLiked = likeRepository.existsByUserIdAndShelterId(userId, shelterId);
```

## 📊 데이터베이스 스키마

### likes 테이블

```sql
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shelter_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (shelter_id) REFERENCES shelters(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_user_shelter (user_id, shelter_id)
);
```

## 🔒 제약사항

- **중복 방지**: 한 사용자가 같은 쉼터를 중복 좋아요할 수 없음
- **참조 무결성**: 사용자나 쉼터가 삭제되면 관련 좋아요도 삭제
- **데이터 정합성**: 외래키 제약조건으로 데이터 무결성 보장

## 📝 향후 확장 계획

### API 기능
- 좋아요 추가/삭제 API
- 사용자별 좋아요 목록 조회
- 쉼터별 좋아요 통계 조회

### 비즈니스 로직
- 좋아요 기반 추천 시스템
- 인기 쉼터 랭킹
- 사용자 선호도 분석

## 🔗 연관 도메인

- **User Domain**: 사용자 정보
- **Shelter Domain**: 쉼터 정보
- **Recommendation System**: 좋아요 기반 추천 