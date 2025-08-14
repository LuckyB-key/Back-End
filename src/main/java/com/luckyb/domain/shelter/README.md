# 쉼터 도메인 (Shelter Domain)

폭염 쉼터 관리, AI 기반 추천, 혼잡도 예측 기능을 담당합니다.

## 📋 API 명세

### 1. 쉼터 목록 조회 (위치 기반)

**Endpoint:** `GET /api/v1/shelters`

**쿼리 파라미터:**
- `lat` (필수): 위도 (Double)
- `lng` (필수): 경도 (Double)
- `distance` (선택): 검색 반경 (km, 기본값: 5km)
- `type` (선택): `public`, `private`, `commercial`, `community`
- `facilities` (선택): 편의시설 필터 (쉼표 구분)

**요청 예시:**
```
GET /api/v1/shelters?lat=37.5547&lng=126.9706&distance=10&type=public&facilities=WiFi,에어컨
```

**응답:**
```json
{
  "success": true,
  "data": [
    {
      "id": "e9a3584e-ce80-4a1f-9b80-c2f62e966086",
      "name": "서울역 쉼터",
      "address": "서울특별시 중구 세종대로 18길",
      "distance": 0.0,
      "status": "active",
      "predictedCongestion": "보통",
      "coordinates": {
        "lat": 37.5547,
        "lng": 126.9706
      }
    }
  ]
}
```

### 2. 쉼터 상세 조회

**Endpoint:** `GET /api/v1/shelters/{shelterId}`

**응답:**
```json
{
  "success": true,
  "data": {
    "id": "e9a3584e-ce80-4a1f-9b80-c2f62e966086",
    "name": "서울역 쉼터",
    "address": {
      "jibun": "서울특별시 중구 세종대로 18길",
      "road": "서울특별시 중구 세종대로 18길"
    },
    "coordinates": {
      "lat": 37.5547,
      "lng": 126.9706
    },
    "type": "public",
    "capacity": 30,
    "facilities": ["에어컨", "화장실", "의자"],
    "operatingHours": "24시간",
    "status": "active",
    "likeCount": 0,
    "reviewCount": 0
  }
}
```

### 3. 쉼터 등록

**Endpoint:** `POST /api/v1/shelters`

**요청 본문:**
```json
{
  "name": "테스트 쉼터",
  "address": {
    "jibun": "서울특별시 강남구 역삼동 123-45",
    "road": "서울특별시 강남구 테헤란로 123"
  },
  "coordinates": {
    "lat": 37.4995,
    "lng": 127.0357
  },
  "type": "public",
  "capacity": 50,
  "facilities": ["에어컨", "WiFi", "화장실", "정수기"],
  "operatingHours": "09:00-18:00"
}
```

**응답:**
```json
{
  "success": true,
  "data": {
    "id": "9ba99b68-4367-488d-a77b-4d16a10bf397",
    "name": "테스트 쉼터",
    "status": "active"
  }
}
```

### 4. 쉼터 수정

**Endpoint:** `PUT /api/v1/shelters/{shelterId}`

**요청 본문:** (부분 업데이트 지원)
```json
{
  "name": "수정된 쉼터 이름",
  "capacity": 60,
  "description": "수정된 설명"
}
```

### 5. 쉼터 삭제 (소프트 삭제)

**Endpoint:** `DELETE /api/v1/shelters/{shelterId}`

**응답:**
```json
{
  "success": true,
  "data": {
    "id": "9ba99b68-4367-488d-a77b-4d16a10bf397",
    "message": "쉼터가 성공적으로 삭제되었습니다."
  }
}
```

### 6. AI 쉼터 추천

**Endpoint:** `GET /api/v1/shelters/recommendations`

**쿼리 파라미터:**
- `lat` (필수): 위도
- `lng` (필수): 경도
- `distance` (선택): 검색 반경 (km, 기본값: 5km)
- `preferences` (선택): 선호 편의시설 (쉼표 구분)

**요청 예시:**
```
GET /api/v1/shelters/recommendations?lat=37.5547&lng=126.9706&distance=10&preferences=air_conditioning,wifi
```

**응답:**
```json
{
  "success": true,
  "data": [
    {
      "id": "e9a3584e-ce80-4a1f-9b80-c2f62e966086",
      "name": "서울역 쉼터",
      "distance": 0.0,
      "status": "active",
      "facilities": ["에어컨", "화장실", "의자"],
      "predictedCongestion": "매우 혼잡"
    }
  ]
}
```

### 7. 혼잡도 예측

**Endpoint:** `GET /api/v1/shelters/{shelterId}/congestion`

**쿼리 파라미터:**
- `date` (선택): 예측 날짜 (YYYY-MM-DD)
- `time` (선택): 예측 시간 (HH:mm)

**요청 예시:**
```
GET /api/v1/shelters/e9a3584e-ce80-4a1f-9b80-c2f62e966086/congestion?date=2025-01-14&time=14:00
```

**응답:**
```json
{
  "success": true,
  "data": {
    "status": "매우 혼잡",
    "currentOccupancy": 47,
    "predictedOccupancy": 50,
    "capacity": 50
  }
}
```

## 🏗️ 구조

### Entity
- `Shelter.java` - 쉼터 엔티티
- `Address.java` - 주소 임베디드 클래스
- `Coordinates.java` - 좌표 임베디드 클래스

### Controller
- `ShelterController.java` - 쉼터 관련 REST API 엔드포인트

### Service
- `ShelterService.java` - 메인 쉼터 비즈니스 로직
- `LocationService.java` - 위치 계산 서비스 (하버사인 공식)
- `RecommendationService.java` - AI 추천 알고리즘
- `CongestionPredictionService.java` - 혼잡도 예측

### Repository
- `ShelterRepository.java` - 쉼터 데이터 액세스

### DTO
- **요청 DTO**: `ShelterCreateRequest`, `ShelterUpdateRequest`, `ShelterListRequest`, etc.
- **응답 DTO**: `ShelterDetailResponse`, `ShelterListResponse`, `CongestionResponse`, etc.

## 📊 데이터베이스 스키마

### shelters 테이블
```sql
CREATE TABLE shelters (
    shelter_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    jibun_address VARCHAR(255),
    road_address VARCHAR(255),
    latitude FLOAT(53) NOT NULL,
    longitude FLOAT(53) NOT NULL,
    type VARCHAR(255) NOT NULL CHECK (type IN ('PUBLIC','PRIVATE','COMMERCIAL','COMMUNITY')),
    capacity INTEGER NOT NULL,
    operating_hours VARCHAR(100),
    status VARCHAR(255) NOT NULL CHECK (status IN ('ACTIVE','INACTIVE','MAINTENANCE','FULL')),
    like_count INTEGER,
    review_count INTEGER,
    description VARCHAR(500),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6)
);
```

### shelter_facilities 테이블
```sql
CREATE TABLE shelter_facilities (
    shelter_id VARCHAR(255) NOT NULL,
    facility VARCHAR(255),
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id)
);
```

## 🎯 Shelter 엔티티 상세

### 필드 설명
- `shelterId` (String): UUID 기반 고유 식별자
- `name` (String): 쉼터 이름 (최대 100자)
- `address` (Address): 주소 정보 (지번/도로명)
- `coordinates` (Coordinates): 위도/경도 좌표
- `type` (ShelterType): 쉼터 타입
- `capacity` (Integer): 수용 가능 인원
- `facilities` (List<String>): 편의시설 목록
- `operatingHours` (String): 운영 시간
- `status` (ShelterStatus): 운영 상태
- `likeCount` (Integer): 좋아요 수
- `reviewCount` (Integer): 리뷰 수
- `description` (String): 상세 설명

### ShelterType 열거형
```java
public enum ShelterType {
    PUBLIC("public"),           // 공공시설
    PRIVATE("private"),         // 민간시설
    COMMERCIAL("commercial"),   // 상업시설
    COMMUNITY("community");     // 커뮤니티 시설
}
```

### ShelterStatus 열거형
```java
public enum ShelterStatus {
    ACTIVE("active"),           // 운영 중
    INACTIVE("inactive"),       // 운영 중지
    MAINTENANCE("maintenance"), // 점검 중
    FULL("full");              // 만석
}
```

## 🤖 AI 추천 알고리즘

### 추천 점수 계산
AI 추천은 다음 요소들을 종합하여 점수를 계산합니다:

1. **거리 점수 (40% 가중치)**
   - 0-1km: 100점
   - 1-3km: 80점
   - 3-5km: 60점
   - 5km+: 40점 이하

2. **편의시설 매칭 점수 (30% 가중치)**
   - 사용자 선호도와 쉼터 시설의 매칭률 계산

3. **수용 인원 점수 (20% 가중치)**
   - 큰 쉼터일수록 높은 점수

4. **혼잡도 점수 (10% 가중치)**
   - 덜 혼잡할수록 높은 점수

### 시간대별 가중치
- **폭염 시간대 (10-18시)**: 1.2배 가중치
- **일반 시간대 (8-21시)**: 1.0배
- **새벽/밤 (21-8시)**: 0.8배

## 📈 혼잡도 예측

### 예측 알고리즘
혼잡도는 다음 요소들을 고려하여 예측됩니다:

1. **시간대별 패턴**
   - 피크 시간: 11-14시 (점심), 17-20시 (저녁)
   - 일반 시간: 8-21시
   - 한산한 시간: 새벽/밤

2. **요일별 가중치**
   - 평일: 1.0배
   - 금요일: 1.1배
   - 토요일: 1.3배
   - 일요일: 1.2배

3. **계절별 가중치**
   - 여름철 (6-8월): 1.5배 (폭염 시즌)
   - 겨울철 (12-2월): 1.2배 (한파 시즌)
   - 봄/가을: 0.8배

4. **쉼터 타입별 조정**
   - 공공시설: 1.2배 (높은 이용률)
   - 커뮤니티: 1.0배
   - 민간시설: 0.9배
   - 상업시설: 0.8배

### 혼잡도 상태
- **한산함**: 20% 미만 점유
- **여유로움**: 20-40% 점유
- **보통**: 40-70% 점유
- **혼잡**: 70-90% 점유
- **매우 혼잡**: 90% 이상 점유

## 🗺️ 위치 기반 서비스

### 거리 계산
하버사인 공식을 사용하여 정확한 직선 거리를 계산합니다:

```java
// 지구 반지름: 6371km
double distance = 6371 * acos(
    cos(radians(lat1)) * cos(radians(lat2)) * 
    cos(radians(lng2) - radians(lng1)) + 
    sin(radians(lat1)) * sin(radians(lat2))
);
```

### 도보 거리 (예정)
추후 카카오맵 API 연동으로 실제 도보 경로 계산 예정

## 🧪 테스트 예시

### 쉼터 등록 (cURL)
```bash
curl -X POST http://localhost:8080/api/v1/shelters \
  -H "Content-Type: application/json" \
  -d '{
    "name": "테스트 쉼터",
    "address": {
      "road": "서울특별시 강남구 테헤란로 123"
    },
    "coordinates": {
      "lat": 37.4995,
      "lng": 127.0357
    },
    "type": "public",
    "capacity": 50,
    "facilities": ["에어컨", "WiFi"],
    "operatingHours": "09:00-18:00"
  }'
```

### 위치 기반 검색 (cURL)
```bash
curl -X GET "http://localhost:8080/api/v1/shelters?lat=37.5547&lng=126.9706&distance=10&type=public"
```

### AI 추천 (cURL)
```bash
curl -X GET "http://localhost:8080/api/v1/shelters/recommendations?lat=37.5547&lng=126.9706&preferences=air_conditioning,wifi"
```

## 🔄 비즈니스 로직

### 쉼터 생성
- UUID 자동 생성
- 기본 상태: ACTIVE
- 좋아요/리뷰 수 0으로 초기화

### 쉼터 삭제
- 소프트 삭제 (상태를 INACTIVE로 변경)
- 실제 데이터는 보존

### 검색 및 필터링
- 위치 기반 반경 검색
- 타입별 필터링
- 편의시설별 필터링
- 거리순 정렬

### 예외 처리
- `ShelterNotFoundException`: 쉼터를 찾을 수 없음 (404)
- `IllegalArgumentException`: 잘못된 좌표/입력값 (400)

## 🔮 향후 확장 계획

- **실시간 혼잡도**: QR 체크인/아웃 시스템
- **카카오맵 연동**: 실제 도보 경로 및 시간
- **날씨 API**: 실시간 날씨 기반 추천
- **이미지 업로드**: 쉼터 사진 관리
- **리뷰 시스템**: 사용자 후기 및 평점 