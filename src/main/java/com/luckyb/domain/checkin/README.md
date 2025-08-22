# QR 체크인 기능

## 개요
QR 코드를 통한 대피소 체크인/체크아웃 기능을 제공합니다.

## API 명세

### QR 체크인 (POST /api/v1/shelters/{shelterId}/checkins)

사용자가 QR 코드를 스캔하여 대피소에 체크인합니다.

**요청:**
- Method: `POST`
- URL: `/api/v1/shelters/{shelterId}/checkins`
- Path Variable: `shelterId` (String) - 대피소 ID
- Authentication: JWT 토큰 필요

**응답:**
```json
{
  "success": true,
  "data": {
    "checkinId": "string",
    "timestamp": "string" // ISO 8601 형식
  }
}
```

**에러 케이스:**
- 404: 대피소를 찾을 수 없음
- 400: 이미 체크인된 상태
- 401: 인증 실패

### 내 체크인 기록 (GET /api/v1/users/me/checkins)

사용자의 체크인 기록을 조회합니다.

**요청:**
- Method: `GET`
- URL: `/api/v1/users/me/checkins`
- Query Parameters:
  - `page`: `number` (선택, 기본값: 0)
  - `size`: `number` (선택, 기본값: 10)
- Authentication: JWT 토큰 필요

**응답:**
```json
{
  "success": true,
  "data": [
    {
      "checkinId": "string",
      "shelterId": "string",
      "shelterName": "string",
      "checkinTime": "string", // ISO 8601 형식
      "checkoutTime": "string" // ISO 8601 형식 (체크아웃하지 않은 경우 null)
    }
  ]
}
```

## 데이터베이스 스키마

### checkins 테이블
```sql
CREATE TABLE checkins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    shelter_id VARCHAR(255) NOT NULL,
    checkin_time DATETIME NOT NULL,
    checkout_time DATETIME NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id)
);
```

## 주요 기능

1. **체크인**: 사용자가 대피소에 체크인
2. **중복 체크인 방지**: 이미 체크인된 상태에서는 추가 체크인 불가
3. **체크인 기록 조회**: 사용자의 모든 체크인 기록 조회 (페이징 지원)
4. **실시간 인원수 조회**: 대피소별 현재 체크인된 인원수 조회

## 보안

- JWT 토큰을 통한 사용자 인증
- 사용자별 체크인 기록 접근 제어
- 대피소 존재 여부 검증 