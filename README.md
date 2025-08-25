# Lucky B-Key - 폭염 쉼터 추천 및 관리 시스템

Lucky B-Key는 AI 기반 폭염 대응 쉼터 추천 및 관리를 위한 Spring Boot REST API 서버입니다.

## 🚀 기술 스택

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Database**: MariaDB (운영), H2 (테스트)
- **ORM**: JPA/Hibernate
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle
- **Cloud Storage**: AWS S3
- **AI**: OpenAI GPT API

## 🌟 주요 기능

### 🤖 AI 기반 기능
- **스마트 쉼터 추천**: OpenAI 기반 거리, 편의시설, 용량, 혼잡도를 종합한 AI 추천
- **혼잡도 예측**: OpenAI 기반 시간대, 요일, 계절별 패턴 분석을 통한 혼잡도 예측
- **위치 기반 서비스**: 하버사인 공식을 활용한 정확한 거리 계산

### 🏠 쉼터 관리
- **실시간 쉼터 검색**: 위치, 타입, 편의시설 기반 필터링
- **쉼터 등록/수정/삭제**: 완전한 CRUD 기능
- **상세 정보 제공**: 주소, 좌표, 운영시간, 편의시설 등
- **이미지 관리**: S3를 통한 쉼터 사진 업로드/관리

### 👤 사용자 관리
- **UUID 기반 인증**: 간편한 사용자 등록 및 로그인
- **개인화 서비스**: 사용자 선호도 기반 맞춤 추천
- **프로필 관리**: 사용자 정보 수정 및 관리

### 🎫 쿠폰 시스템
- **쿠폰 등록/관리**: 비즈니스 사용자용 쿠폰 등록 및 관리
- **쿠폰 발급/사용**: 사용자 쿠폰 발급 및 사용 처리
- **쿠폰 조회**: 개인 쿠폰 및 비즈니스 쿠폰 목록 조회

### 📱 체크인 시스템
- **QR코드 생성**: 쉼터별 고유 QR코드 생성
- **체크인 처리**: QR코드 스캔을 통한 쉼터 방문 기록
- **방문 이력**: 사용자별 체크인 히스토리 관리

### 💬 리뷰 시스템
- **리뷰 작성**: 쉼터 이용 후 리뷰 및 평점 작성
- **리뷰 관리**: 리뷰 수정, 삭제 기능
- **리뷰 조회**: 쉼터별, 사용자별 리뷰 목록 조회

### 🔔 알림 시스템
- **실시간 알림**: 사용자 활동 기반 알림 발송
- **알림 관리**: 알림 읽음 처리 및 히스토리 관리

### 📢 공지사항 & 광고
- **공지사항 관리**: 시스템 공지사항 등록 및 관리
- **AI 맞춤 광고**: 사용자 선호도 기반 맞춤 광고 추천

## 🔧 설정 및 실행

### 1. 데이터베이스 설정 (MariaDB)

MariaDB 설치 후 데이터베이스를 생성합니다:

```sql
-- MariaDB 접속
mysql -u root -p

-- 데이터베이스 생성
CREATE DATABASE lucky_b_key CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여 (선택사항)
CREATE USER 'luckyb'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON lucky_b_key.* TO 'luckyb'@'localhost';
FLUSH PRIVILEGES;
```

**💡 데이터베이스 URL 최적화:**
- 기본 URL: `jdbc:mariadb://localhost:3306/lucky_b_key?serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true`
- 필수 보안 파라미터만 유지하여 안정성 확보
- Spring Boot 자동 설정으로 대부분의 옵션이 자동 처리됨

### 2. 환경 변수 설정

다음 환경 변수를 설정하거나 application.yml의 기본값을 사용:

```bash
# 데이터베이스 설정
export DB_USERNAME=root
export DB_PASSWORD=your_mariadb_password

# JWT 보안 키 (최소 512비트 필수 - HS512 알고리즘 요구사항)
export JWT_SECRET=your_super_secret_jwt_key_here_make_it_at_least_512_bits_long_for_hs512_algorithm_security_requirement

# OpenAI API 키 (AI 기능 사용 시 필수)
export OPENAI_API_KEY=your_openai_api_key_here

# AWS S3 설정 (파일 업로드 기능 사용 시 필수)
export AWS_ACCESS_KEY_ID=your_aws_access_key
export AWS_SECRET_ACCESS_KEY=your_aws_secret_key
export AWS_S3_BUCKET_NAME=your_s3_bucket_name
export AWS_S3_REGION=ap-northeast-2
```

#### .env 파일 사용 (권장)
프로젝트 루트에 `.env` 파일을 생성하고 환경 변수를 설정할 수 있습니다:

```bash
# .env 파일 예시
DB_USERNAME=root
DB_PASSWORD=your_mariadb_password
JWT_SECRET=your_super_secret_jwt_key_here_make_it_at_least_512_bits_long_for_hs512_algorithm_security_requirement
OPENAI_API_KEY=your_openai_api_key_here
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
AWS_S3_BUCKET_NAME=your_s3_bucket_name
AWS_S3_REGION=ap-northeast-2
```

**⚠️ 보안 주의사항:**
- `.env` 파일은 `.gitignore`에 포함되어 있어 Git에 커밋되지 않습니다
- API 키와 같은 민감한 정보는 절대 Git에 커밋하지 마세요
- 프로덕션 환경에서는 반드시 환경변수로 JWT_SECRET을 설정하세요
- JWT_SECRET은 최소 512비트(64바이트) 이상이어야 HS512 알고리즘이 정상 작동합니다
- OpenAI API 키는 반드시 환경변수로 설정하세요
- AWS S3 키는 반드시 환경변수로 설정하세요
- 기본값은 개발용이므로 프로덕션에서는 사용하지 마세요
- DB_PASSWORD는 반드시 강력한 비밀번호를 사용하세요

### 3. 애플리케이션 실행

```bash
# 애플리케이션 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/lucky-b-key-0.0.1-SNAPSHOT.jar
```

### 4. 테스트 환경 실행

테스트 환경에서는 H2 인메모리 데이터베이스를 사용합니다:

```bash
# 테스트 프로파일로 실행
./gradlew bootRun --args='--spring.profiles.active=test'
```

테스트 환경 H2 콘솔: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (공백)

### 5. 애플리케이션 접속

- **API 서버**: http://localhost:8080
- **H2 콘솔**: http://localhost:8080/h2-console (테스트 환경만)
- **Swagger UI**: http://localhost:8080/swagger-ui.html

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
- `GET /api/v1/shelters/recommendations` - AI 쉼터 추천
- `GET /api/v1/shelters/{id}/congestion` - AI 혼잡도 예측

### 📱 체크인 API
- `POST /api/v1/checkin` - QR코드 스캔을 통한 체크인
- `GET /api/v1/checkin/my-checkins` - 내 체크인 히스토리 조회
- `GET /api/v1/qr/{shelterId}` - 쉼터별 QR코드 생성

### 💬 리뷰 API
- `GET /api/v1/reviews` - 리뷰 목록 조회
- `POST /api/v1/reviews` - 리뷰 작성
- `GET /api/v1/reviews/{id}` - 리뷰 상세 조회
- `PUT /api/v1/reviews/{id}` - 리뷰 수정
- `DELETE /api/v1/reviews/{id}` - 리뷰 삭제
- `GET /api/v1/reviews/my-reviews` - 내가 작성한 리뷰 목록

### 🔔 알림 API
- `GET /api/v1/notifications` - 알림 목록 조회
- `PUT /api/v1/notifications/{id}/read` - 알림 읽음 처리

### 📢 공지사항 API
- `GET /api/v1/announcements` - 공지사항 목록 조회 (페이징)
- `POST /api/v1/announcements` - 공지사항 등록
- `GET /api/v1/announcements/{id}` - 공지사항 상세 조회
- `PUT /api/v1/announcements/{id}` - 공지사항 수정
- `DELETE /api/v1/announcements/{id}` - 공지사항 삭제

### 🎫 쿠폰 API
- `GET /api/v1/coupons` - 쿠폰 목록 조회
- `POST /api/v1/coupons` - 쿠폰 등록 (비즈니스 사용자)
- `GET /api/v1/coupons/{id}` - 쿠폰 상세 조회
- `GET /api/v1/coupons/my-business` - 내가 발행한 쿠폰 목록
- `GET /api/v1/coupons/my-coupons` - 내 쿠폰 목록
- `POST /api/v1/coupons/{id}/issue` - 쿠폰 발급
- `POST /api/v1/coupons/{id}/use` - 쿠폰 사용

### 📸 파일 업로드 API
- `POST /api/v1/s3/upload` - S3 파일 업로드
- `GET /api/v1/s3/download/{fileName}` - S3 파일 다운로드

### 📢 광고 API
- `GET /api/v1/advertisements` - 광고 목록 조회
- `POST /api/v1/advertisements` - 광고 등록
- `GET /api/v1/advertisements/{id}` - 광고 상세 조회
- `PUT /api/v1/advertisements/{id}` - 광고 수정
- `DELETE /api/v1/advertisements/{id}` - 광고 삭제
- `GET /api/v1/advertisements/ai-recommendations` - AI 맞춤 광고 추천

## 📁 도메인별 상세 문서

각 도메인의 상세한 API 명세와 엔터티 구조는 해당 폴더의 README 파일을 참고하세요:

- [인증 도메인](src/main/java/com/luckyb/domain/auth/README.md) - 인증 관련 API
- [사용자 도메인](src/main/java/com/luckyb/domain/user/README.md) - 사용자 관리 API  
- [쉼터 도메인](src/main/java/com/luckyb/domain/shelter/README.md) - 쉼터 관리 및 AI 기능 API
- [체크인 도메인](src/main/java/com/luckyb/domain/checkin/README.md) - QR코드 및 체크인 API
- [리뷰 도메인](src/main/java/com/luckyb/domain/review/README.md) - 리뷰 관리 API
- [알림 도메인](src/main/java/com/luckyb/domain/notification/README.md) - 알림 관리 API
- [공지사항 도메인](src/main/java/com/luckyb/domain/announcement/README.md) - 공지사항 관리 API
- [쿠폰 도메인](src/main/java/com/luckyb/domain/coupon/README.md) - 쿠폰 관리 API
- [광고 도메인](src/main/java/com/luckyb/domain/advertisement/README.md) - AI 맞춤 광고 API
- [좋아요 도메인](src/main/java/com/luckyb/domain/like/README.md) - 쉼터 좋아요 기능

## 🗂️ 프로젝트 구조

```
src/main/java/com/luckyb/
├── LuckyBKeyApplication.java
├── domain/
│   ├── auth/                 # 인증 도메인
│   ├── user/                 # 사용자 도메인
│   ├── shelter/              # 쉼터 도메인
│   ├── checkin/              # 체크인 도메인
│   ├── review/               # 리뷰 도메인
│   ├── notification/         # 알림 도메인
│   ├── announcement/         # 공지사항 도메인
│   ├── coupon/               # 쿠폰 도메인
│   ├── advertisement/        # 광고 도메인
│   ├── like/                 # 좋아요 도메인
│   └── imageS3/              # S3 파일 업로드 도메인
└── global/
    ├── common/               # 공통 응답 형식
    ├── config/               # 설정 클래스
    ├── exception/            # 예외 처리
    ├── jwt/                  # JWT 유틸리티
    ├── ai/                   # AI 서비스
    └── util/                 # 유틸리티 (QR코드 생성 등)
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
   5. POST /api/v1/checkin (체크인)
   6. POST /api/v1/reviews (리뷰 작성)
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
- **파일 업로드 보안**: S3 서명된 URL을 통한 안전한 파일 업로드

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

### QR코드 체크인 시스템
- **고유 QR코드**: 쉼터별 고유 QR코드 생성
- **실시간 체크인**: QR코드 스캔을 통한 즉시 체크인
- **방문 히스토리**: 사용자별 체크인 기록 관리

### 파일 관리 시스템
- **S3 통합**: AWS S3를 통한 안전한 파일 저장
- **이미지 업로드**: 쉼터 사진 및 리뷰 이미지 업로드
- **보안 URL**: 서명된 URL을 통한 안전한 파일 접근

## 🔄 확장 계획

- **외부 API 연동**: 카카오맵 API (실제 도보 경로)
- **날씨 API**: 실시간 날씨 기반 혼잡도 가중치
- **실시간 알림**: WebSocket을 통한 실시간 혼잡도 업데이트
- **모바일 앱**: React Native 기반 모바일 애플리케이션
- **관리자 대시보드**: 쉼터 관리 및 통계 대시보드

## 📄 라이선스

이 프로젝트는 Lucky B-Key 팀의 소유입니다. 