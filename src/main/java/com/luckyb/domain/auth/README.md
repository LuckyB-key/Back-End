# 인증 도메인 (Auth Domain)

JWT 기반 사용자 인증 및 토큰 관리를 담당합니다.

## 📋 API 명세

### UUID 생성 및 로그인

새로운 사용자 UUID를 생성하고 JWT 토큰을 발급합니다.

**Endpoint:** `POST /api/v1/auth/uuid`

**요청:**
- **Method**: POST
- **Content-Type**: application/json
- **Body**: 없음

**응답:**
```json
{
  "success": true,
  "data": {
    "uuid": "06ebe902-0295-4488-852d-b130da17df94",
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwNmViZTkwMi0wMjk1LTQ0ODgtODUyZC1iMTMwZGExN2RmOTQiLCJpYXQiOjE3NTUwNjY0MjgsImV4cCI6MTc1NTE1MjgyOH0.OlZw5Nt95UAr3hitNuOupFfvog6bA1TCIqZVb_S0NLEWAembqFwiv4QXnheS_1S_JDUHI9YV5P4FQ4VHIynb5g"
  },
  "message": null
}
```

**응답 필드:**
- `uuid`: 생성된 사용자 고유 식별자
- `accessToken`: JWT 토큰 (24시간 유효)

## 🏗️ 구조

### Controller
- `AuthController.java` - 인증 관련 REST API 엔드포인트

### DTO
- `UuidLoginResponse.java` - UUID 로그인 응답 데이터

### Service
인증 도메인은 `UserService`를 의존하여 사용자 생성을 처리합니다.

## 🔐 JWT 토큰 정보

### 토큰 구성
- **알고리즘**: HS512
- **유효기간**: 24시간 (86400000ms)
- **Subject**: 사용자 UUID
- **Issuer**: Lucky B-Key

### 토큰 사용법
생성된 토큰은 다른 API 호출 시 Authorization 헤더에 포함해야 합니다:

```
Authorization: Bearer {accessToken}
```

## 🧪 테스트 예시

### cURL
```bash
curl -X POST http://localhost:8080/api/v1/auth/uuid \
  -H "Content-Type: application/json"
```

### Postman
1. **Method**: POST
2. **URL**: `http://localhost:8080/api/v1/auth/uuid`
3. **Headers**: 
   - `Content-Type`: `application/json`
4. **Body**: 없음

## 🔄 인증 플로우

1. **클라이언트**: `POST /api/v1/auth/uuid` 호출
2. **서버**: 새 User 엔티티 생성 (기본 역할: USER)
3. **서버**: 생성된 User의 UUID로 JWT 토큰 생성
4. **서버**: UUID와 토큰을 응답으로 반환
5. **클라이언트**: 받은 토큰을 저장하고 이후 API 호출에 사용

## ⚠️ 주의사항

- 토큰은 24시간 후 만료되므로 만료 시 재발급 필요
- UUID는 서버에서 자동 생성되며 중복되지 않음
- 현재 구현에서는 refresh token이 없으므로 토큰 만료 시 새로 발급받아야 함
- 토큰 저장은 클라이언트 책임 (localStorage, sessionStorage 등) 