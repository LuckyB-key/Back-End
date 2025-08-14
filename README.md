# Lucky B-Key - 폭염 쉼터 추천 및 관리 시스템

Lucky B-Key는 AI 기반 폭염 대응 쉼터 추천 및 관리를 위한 Spring Boot REST API 서버입니다.

## 🚀 기술 스택

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Database**: H2 (개발/테스트), PostgreSQL (운영)
- **ORM**: JPA/Hibernate
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle

## 🌟 주요 기능

### 🤖 AI 기반 기능
- **스마트 쉼터 추천**: 거리, 편의시설, 용량, 혼잡도를 종합한 AI 추천
- **혼잡도 예측**: 시간대, 요일, 계절별 패턴 분석을 통한 혼잡도 예측
- **위치 기반 서비스**: 하버사인 공식을 활용한 정확한 거리 계산

### 🏠 쉼터 관리
- **실시간 쉼터 검색**: 위치, 타입, 편의시설 기반 필터링
- **쉼터 등록/수정/삭제**: 완전한 CRUD 기능
- **상세 정보 제공**: 주소, 좌표, 운영시간, 편의시설 등

### 👤 사용자 관리
- **UUID 기반 인증**: 간편한 사용자 등록 및 로그인
- **개인화 서비스**: 사용자 선호도 기반 맞춤 추천

## 🔧 설정 및 실행

### 1. 개발 환경 (H2 Database)

현재 설정은 H2 인메모리 데이터베이스를 사용하여 별도 설정 없이 바로 실행 가능합니다.

```bash
# 애플리케이션 실행
./gradlew bootRun
```

H2 콘솔: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (공백)

### 2. 운영 환경 (PostgreSQL)

application.yml에서 PostgreSQL 설정을 활성화하고 환경 변수를 설정:

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret_key_min_512_bits
```

### 3. 애플리케이션 접속

- **API 서버**: http://localhost:8080
- **H2 콘솔**: http://localhost:8080/h2-console (개발환경)

## 📖 API 개요

### 🔐 인증 API
- `POST /api/v1/auth/uuid` - UUID 생성 및 로그인

### 👤 사용자 API  
- `GET /api/v1/users/me` - 내 정보 조회
- `PUT /api/v1/users/me` - 내 정보 수정

### 🏠 쉼터 API
- `GET /api/v1/shelters` - 쉼터 목록 조회 (위치 기반)
- `POST /api/v1/shelters` - 쉼터 등록
- `GET /api/v1/shelters/{id}` - 쉼터 상세 조회
- `PUT /api/v1/shelters/{id}` - 쉼터 수정
- `DELETE /api/v1/shelters/{id}` - 쉼터 삭제

### 🤖 AI 기능 API
- `GET /api/v1/shelters/recommendations` - AI 쉼터 추천
- `GET /api/v1/shelters/{id}/congestion` - 혼잡도 예측

## 📁 도메인별 상세 문서

각 도메인의 상세한 API 명세와 엔터티 구조는 해당 폴더의 README 파일을 참고하세요:

- [인증 도메인](src/main/java/com/luckyb/domain/auth/README.md) - 인증 관련 API
- [사용자 도메인](src/main/java/com/luckyb/domain/user/README.md) - 사용자 관리 API  
- [쉼터 도메인](src/main/java/com/luckyb/domain/shelter/README.md) - 쉼터 관리 및 AI 기능 API

## 🗂️ 프로젝트 구조

```
src/main/java/com/luckyb/
├── LuckyBKeyApplication.java
├── domain/
│   ├── auth/                 # 인증 도메인
│   ├── user/                 # 사용자 도메인
│   └── shelter/              # 쉼터 도메인
└── global/
    ├── common/               # 공통 응답 형식
    ├── config/               # 설정 클래스
    ├── exception/            # 예외 처리
    └── jwt/                  # JWT 유틸리티
```

## 🧪 테스트

### Postman으로 API 테스트

1. **기본 설정**
   - Base URL: `http://localhost:8080`
   - Content-Type: `application/json`

2. **테스트 순서**
   ```
   1. POST /api/v1/auth/uuid (토큰 발급)
   2. POST /api/v1/shelters (쉼터 등록)
   3. GET /api/v1/shelters (목록 조회)
   4. GET /api/v1/shelters/recommendations (AI 추천)
   ```

### Gradle 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 빌드 및 테스트
./gradlew build
```

## 🔒 보안 및 인증

- **JWT 토큰**: 24시간 유효기간
- **Spring Security**: API 보안 및 인증 관리
- **CORS 설정**: 프론트엔드 도메인 허용
- **입력값 검증**: DTO 레벨 유효성 검사
- **예외 처리**: 구조화된 에러 응답

## 🌟 주요 특징

### AI 추천 알고리즘
- **다중 요소 분석**: 거리, 편의시설, 용량, 혼잡도 종합 평가
- **시간대별 가중치**: 폭염 시간대(10-18시) 높은 가중치 적용
- **개인화**: 사용자 선호도 기반 맞춤 추천

### 혼잡도 예측
- **시간대별 패턴**: 피크 시간대 (11-14시, 17-20시) 반영
- **요일별 가중치**: 주말 vs 평일 이용 패턴 차이
- **계절별 조정**: 여름철(6-8월) 높은 이용률 반영

### 위치 기반 서비스
- **정확한 거리 계산**: 하버사인 공식 사용
- **효율적인 검색**: 반경 기반 필터링
- **다중 필터**: 타입, 편의시설, 거리 동시 적용

## 🔄 확장 계획

- **외부 API 연동**: 카카오맵 API (실제 도보 경로)
- **날씨 API**: 실시간 날씨 기반 혼잡도 가중치
- **실시간 알림**: WebSocket을 통한 실시간 혼잡도 업데이트
- **이미지 업로드**: 쉼터 사진 관리 기능

## 📄 라이선스

이 프로젝트는 Lucky B-Key 팀의 소유입니다. 