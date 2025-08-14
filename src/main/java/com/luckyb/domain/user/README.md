# 사용자 도메인 (User Domain)

사용자 정보 관리 및 개인화 설정을 담당합니다.

## 📋 API 명세

### 내 정보 조회

현재 로그인한 사용자의 정보를 조회합니다.

**Endpoint:** `GET /api/v1/users/me`

**요청:**
- **Method**: GET
- **Headers**: `Authorization: Bearer {JWT_TOKEN}`

**응답:**
```json
{
  "success": true,
  "data": {
    "userId": "06ebe902-0295-4488-852d-b130da17df94",
    "nickname": "사용자닉네임",
    "email": "user@example.com",
    "role": "user",
    "preferences": ["에어컨", "WiFi", "화장실"]
  },
  "message": null
}
```

### 내 정보 수정

현재 로그인한 사용자의 정보를 수정합니다.

**Endpoint:** `PUT /api/v1/users/me`

**요청:**
- **Method**: PUT
- **Headers**: `Authorization: Bearer {JWT_TOKEN}`
- **Content-Type**: application/json

**요청 본문:**
```json
{
  "nickname": "새로운닉네임",
  "email": "newemail@example.com",
  "preferences": ["에어컨", "WiFi", "화장실", "정수기"]
}
```

**응답:**
```json
{
  "success": true,
  "data": {
    "userId": "06ebe902-0295-4488-852d-b130da17df94",
    "nickname": "새로운닉네임",
    "email": "newemail@example.com",
    "role": "user",
    "preferences": ["에어컨", "WiFi", "화장실", "정수기"]
  },
  "message": null
}
```

## 🏗️ 구조

### Entity
- `User.java` - 사용자 엔티티

### Controller
- `UserController.java` - 사용자 관련 REST API 엔드포인트

### Service
- `UserService.java` - 사용자 비즈니스 로직

### Repository
- `UserRepository.java` - 사용자 데이터 액세스

### DTO
- `UserMeResponse.java` - 사용자 정보 응답
- `UserMeUpdateRequest.java` - 사용자 정보 수정 요청

## 📊 데이터베이스 스키마

### users 테이블
```sql
CREATE TABLE users (
    user_id VARCHAR(255) PRIMARY KEY,
    nickname VARCHAR(50),
    email VARCHAR(100),
    role VARCHAR(255) NOT NULL CHECK (role IN ('USER','BUSINESS','ADMIN')),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6)
);
```

### user_preferences 테이블
```sql
CREATE TABLE user_preferences (
    user_id VARCHAR(255) NOT NULL,
    preference VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

## 🎯 User 엔티티 상세

### 필드 설명
- `userId` (String): UUID 기반 고유 식별자
- `nickname` (String): 사용자 닉네임 (최대 50자)
- `email` (String): 이메일 주소 (최대 100자)
- `role` (UserRole): 사용자 역할 (USER, BUSINESS, ADMIN)
- `preferences` (List<String>): 선호 편의시설 목록
- `createdAt` (LocalDateTime): 생성 일시
- `updatedAt` (LocalDateTime): 수정 일시

### UserRole 열거형
```java
public enum UserRole {
    USER,       // 일반 사용자
    BUSINESS,   // 사업자 (쉼터 등록 권한)
    ADMIN       // 관리자
}
```

### 주요 메서드
- `createDefaultUser()`: 기본 사용자 생성 (정적 팩토리 메서드)
- `updateInfo()`: 사용자 정보 수정

## 🧪 테스트 예시

### 내 정보 조회 (cURL)
```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 내 정보 수정 (cURL)
```bash
curl -X PUT http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "새로운닉네임",
    "email": "new@example.com",
    "preferences": ["에어컨", "WiFi"]
  }'
```

### Postman 설정

#### 내 정보 조회
1. **Method**: GET
2. **URL**: `http://localhost:8080/api/v1/users/me`
3. **Headers**: 
   - `Authorization`: `Bearer YOUR_JWT_TOKEN`

#### 내 정보 수정
1. **Method**: PUT
2. **URL**: `http://localhost:8080/api/v1/users/me`
3. **Headers**: 
   - `Authorization`: `Bearer YOUR_JWT_TOKEN`
   - `Content-Type`: `application/json`
4. **Body** (raw JSON):
   ```json
   {
     "nickname": "새로운닉네임",
     "email": "new@example.com",
     "preferences": ["에어컨", "WiFi", "화장실"]
   }
   ```

## 🔄 비즈니스 로직

### 사용자 생성
- UUID 자동 생성
- 기본 역할: USER
- 빈 선호도 목록으로 초기화

### 사용자 정보 수정
- 닉네임 중복 검사
- 이메일 중복 검사
- 부분 업데이트 지원 (null 값은 무시)

### 예외 처리
- `UserNotFoundException`: 사용자를 찾을 수 없음 (404)
- `InvalidTokenException`: 토큰이 유효하지 않음 (401)
- `IllegalArgumentException`: 중복된 닉네임/이메일 (400)

## 💡 개인화 기능

### 선호도 관리
사용자는 다음과 같은 편의시설을 선호도로 설정할 수 있습니다:
- "에어컨" - 냉방 시설
- "WiFi" - 무선 인터넷
- "화장실" - 화장실 시설
- "정수기" - 식수 제공
- "휴게실" - 휴식 공간
- "의자" - 좌석 시설

이 선호도는 AI 쉼터 추천 시 가중치로 활용됩니다. 