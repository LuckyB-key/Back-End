# 광고 도메인 (Advertisement Domain)

위치 기반 AI 맞춤 광고 추천 기능을 제공하는 모듈입니다.

## 🎯 주요 기능

- **AI 맞춤 광고 추천**: 위치 기반 개인화된 광고 추천
- **광고 관리**: 광고 CRUD 기능
- **사용자 개인화**: 사용자 ID 기반 맞춤 광고

## 🏗️ 구조

```
domain/advertisement/
├── controller/
│   └── AdvertisementController.java    # 광고 API 컨트롤러
├── service/
│   └── AdvertisementService.java       # 광고 비즈니스 로직
├── repository/
│   └── AdvertisementRepository.java    # 광고 데이터 접근
├── entity/
│   └── Advertisement.java              # 광고 엔티티
├── dto/
│   ├── AdvertisementRequest.java       # 광고 요청 DTO
│   ├── AdvertisementResponse.java      # 광고 응답 DTO
│   ├── AdListRequest.java              # AI 광고 요청 DTO
│   └── AdListResponse.java             # AI 광고 응답 DTO
└── README.md                           # 이 파일
```

## 📋 API 명세

### AI 맞춤 광고 추천

**경로**: `GET /api/v1/advertisements/ai-recommendations`

**요청 파라미터**:
- `lat` (필수): 위도 (Double)
- `lng` (필수): 경도 (Double)
- `userId` (선택): 사용자 ID (String)

**응답 예시**:
```json
{
  "success": true,
  "data": [
    {
      "id": "ad_001",
      "adType": "location_based",
      "content": "가까운 무더위 쉼터 안내",
      "businessName": "서울시청",
      "image": "https://example.com/image.jpg"
    }
  ],
  "message": null
}
```

### 광고 관리 API

#### 광고 등록
- **경로**: `POST /api/v1/advertisements`
- **요청**: `AdvertisementRequest`
- **응답**: `AdvertisementResponse`

#### 광고 수정
- **경로**: `PUT /api/v1/advertisements/{id}`
- **요청**: `AdvertisementRequest`
- **응답**: `AdvertisementResponse`

#### 광고 조회
- **경로**: `GET /api/v1/advertisements/{id}`
- **응답**: `AdvertisementResponse`

#### 광고 목록 조회
- **경로**: `GET /api/v1/advertisements`
- **응답**: `Page<AdvertisementResponse>`

#### 광고 삭제
- **경로**: `DELETE /api/v1/advertisements/{id}`
- **응답**: `ApiResponse<String>`

## 🔧 사용 예시

### AI 맞춤 광고 추천

```bash
# 사용자 ID와 함께 요청
curl -X GET "http://localhost:8080/api/v1/advertisements/ai-recommendations?lat=37.5665&lng=126.9780&userId=test-user"

# 사용자 ID 없이 요청
curl -X GET "http://localhost:8080/api/v1/advertisements/ai-recommendations?lat=37.5665&lng=126.9780"
```

### 광고 등록

```bash
curl -X POST "http://localhost:8080/api/v1/advertisements" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "무더위 쉼터 안내",
    "content": "가까운 무더위 쉼터를 찾아보세요"
  }'
```

## 🤖 AI 기능

### OpenAI 통합
- **모델**: GPT-3.5-turbo
- **프롬프트**: 위치 기반 맞춤 광고 생성
- **응답 형식**: JSON 구조화된 광고 데이터

### 에러 처리
- AI 서비스 실패 시 기본 광고 반환
- 안전한 데이터 매핑
- 상세한 로그 기록

## 📊 데이터 모델

### Advertisement 엔티티
```java
@Entity
public class Advertisement {
    private Long id;
    private User user;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### AdListResponse DTO
```java
public class AdListResponse {
    private String id;
    private String adType;        // "banner", "location_based"
    private String content;
    private String businessName;
    private String image;
}
```

## 🔒 보안

- 사용자 인증 기반 광고 관리
- 위치 데이터 검증
- API 키 보안 (환경변수 사용)

## 📝 로깅

- AI 광고 추천 요청/응답 로그
- 에러 상황 상세 로그
- 성능 모니터링 로그 