# 공지사항 도메인 (Announcement Domain)

공지사항 관리 기능을 제공하는 도메인입니다.

## 📋 API 명세

### 1. 공지사항 등록
- **URL**: `POST /api/v1/announcements`
- **Description**: 새로운 공지사항을 등록합니다.

**Request Body:**
```json
{
  "title": "공지사항 제목",
  "content": "공지사항 내용",
  "author": "작성자"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "announcementId": "uuid-string",
    "title": "공지사항 제목",
    "content": "공지사항 내용",
    "author": "작성자",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 2. 공지사항 수정
- **URL**: `PUT /api/v1/announcements/{announcementId}`
- **Description**: 기존 공지사항을 수정합니다.

**Request Body:**
```json
{
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "announcementId": "uuid-string",
    "title": "수정된 제목",
    "content": "수정된 내용",
    "author": "작성자",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T11:00:00"
  }
}
```

### 3. 공지사항 목록 조회
- **URL**: `GET /api/v1/announcements`
- **Description**: 공지사항 목록을 페이징하여 조회합니다.

**Query Parameters:**
- `page` (optional): 페이지 번호 (기본값: 0)
- `size` (optional): 페이지 크기 (기본값: 10)

**Response:**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "announcementId": "uuid-string",
        "title": "공지사항 제목",
        "content": "공지사항 내용",
        "createdAt": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

### 4. 공지사항 상세 조회
- **URL**: `GET /api/v1/announcements/{announcementId}`
- **Description**: 특정 공지사항의 상세 정보를 조회합니다.

**Response:**
```json
{
  "success": true,
  "data": {
    "announcementId": "uuid-string",
    "title": "공지사항 제목",
    "content": "공지사항 내용",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 5. 공지사항 삭제
- **URL**: `DELETE /api/v1/announcements/{announcementId}`
- **Description**: 공지사항을 삭제합니다.

**Response:**
```json
{
  "success": true,
  "data": {
    "message": "공지사항이 성공적으로 삭제되었습니다.",
    "announcementId": "uuid-string"
  }
}
```

## 🗂️ 도메인 구조

```
announcement/
├── controller/
│   └── AnnouncementController.java    # REST API 컨트롤러
├── dto/
│   ├── AnnouncementCreateRequest.java     # 생성 요청 DTO
│   ├── AnnouncementCreateResponse.java    # 생성 응답 DTO
│   ├── AnnouncementUpdateRequest.java     # 수정 요청 DTO
│   ├── AnnouncementUpdateResponse.java    # 수정 응답 DTO
│   ├── AnnouncementListRequest.java       # 목록 조회 요청 DTO
│   ├── AnnouncementListResponse.java      # 목록 조회 응답 DTO
│   ├── AnnouncementDetailResponse.java    # 상세 조회 응답 DTO
│   └── AnnouncementDeleteResponse.java    # 삭제 응답 DTO
├── entity/
│   └── Announcement.java              # 공지사항 엔티티
├── repository/
│   └── AnnouncementRepository.java    # 데이터 접근 계층
├── service/
│   └── AnnouncementService.java       # 비즈니스 로직
└── README.md                          # 이 파일
```

## 🔧 주요 기능

### 1. CRUD 작업
- **Create**: 새로운 공지사항 등록
- **Read**: 공지사항 목록 조회 및 상세 조회
- **Update**: 기존 공지사항 수정
- **Delete**: 공지사항 삭제

### 2. 페이징 지원
- 목록 조회 시 페이지 단위로 데이터 제공
- 기본 페이지 크기: 10개
- 페이지 번호는 0부터 시작

### 3. 자동 타임스탬프
- `createdAt`: 생성 시간 (자동 설정)
- `updatedAt`: 수정 시간 (자동 업데이트)

### 4. UUID 기반 ID
- 각 공지사항은 고유한 UUID를 가짐
- 보안성과 확장성 확보

## 🚨 예외 처리

### AnnouncementNotFoundException
- 존재하지 않는 공지사항 ID로 조회/수정/삭제 시 발생
- HTTP 404 상태 코드 반환

## 📝 사용 예시

### Postman 테스트

1. **공지사항 등록**
   ```
   POST http://localhost:8080/api/v1/announcements
   Content-Type: application/json
   
   {
     "title": "시스템 점검 안내",
     "content": "2024년 1월 15일 새벽 2시부터 4시까지 시스템 점검이 예정되어 있습니다.",
     "author": "시스템 관리자"
   }
   ```

2. **공지사항 목록 조회**
   ```
   GET http://localhost:8080/api/v1/announcements?page=0&size=5
   ```

3. **공지사항 수정**
   ```
   PUT http://localhost:8080/api/v1/announcements/{announcementId}
   Content-Type: application/json
   
   {
     "title": "시스템 점검 일정 변경 안내",
     "content": "시스템 점검이 1월 16일로 변경되었습니다."
   }
   ```

## 🔒 보안 고려사항

- 현재는 인증/인가 없이 모든 API 접근 가능
- 향후 관리자 권한 체크 추가 필요
- 작성자 정보는 현재 요청에서 받지만, 실제로는 인증된 사용자 정보 사용 권장 