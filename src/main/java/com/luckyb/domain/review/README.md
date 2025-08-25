# 리뷰 도메인(Review Domain)

---
리뷰 관리 기능을 제공하는 도메인입니다.

## 📄 API 명세

---

### 1. 리뷰 작성

* #### URL:  `Post /api/v1/shelters/{shelterId}/reviews`
* #### Description: 새로운 리뷰를 등록합니다.

### Request Body:

```json
{
  "text": "이 쉼터 정말 깨끗하고 좋아요!",
  "rating": 5,
  "userNickname": "사용자",
  "userId": "user123",
  "photoUrls": [
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg"
  ]
}

```

### Response:

```json
{
  "success": true,
  "data": {
    "id": "9",
    "shelterId": "123",
    "userNickname": null,
    "userId": "user123",
    "text": "이 쉼터 정말 깨끗하고 좋아요!",
    "rating": 5,
    "createdAt": "2025-08-19T16:21:15.0212193",
    "updatedAt": "2025-08-19T16:21:15.0212193"
  }
}
```

### 2. 리뷰 수정

* #### URL:  `PUT /api/v1/reviews/{reviewId} `
* #### Description: 기존의 리뷰를 수정합니다.

### Request Body:

```json
{
  "text": "수정된 리뷰 내용",
  "rating": 3,
  "photoUrls": [
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg"
  ]
}

```

### Response:

```json
{
  "success": true,
  "data": {
    "id": "3",
    "text": "수정된 리뷰 내용",
    "rating": 3,
    "createdAt": "2025-08-19T16:23:17.8512868",
    "updatedAt": "2025-08-19T16:23:17.8512868"
  }
}
```

### 3. 리뷰 삭제

* #### URL:  `DELETE /api/v1/reviews/{reviewId} `
* #### Description: 리뷰를 삭제합니다.

### Response Body:

```json
{
  "success": true,
  "data": {
    "message": "리뷰가 삭제되었습니다."
  }
}

```

### 4. 리뷰 목록 조회

* #### URL:  `GET /api/v1/shelters/{shelterId}/reviews `
* #### Description: 리뷰 목록을 페이징하여 조회합니다.

#### Query Parameters:

* #### `page` (optional): 페이지 번호 (기본값: 0)
* #### `size` (optional): 페이지 크기 (기본값: 10)

### Response:

```json
 {
  "id": "3",
  "userId": "user123",
  "userNickname": "사용자",
  "text": "이 쉼터 정말 깨끗하고 좋아요!",
  "rating": 5,
  "photoUrls": [
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg"
  ],
  "createdAt": "2025-08-18T16:19:29"
}
```

### 5. 내 리뷰 목록 조회(특정 사용자)

* #### URL:  `GET "/api/v1/users/{userId}/reviews" `
* #### Description: 특정 사용자의 리뷰 목록을  페이징하여 조회합니다.

### Request Body:

#### Query Parameters:

* #### `page` (optional): 페이지 번호 (기본값: 0)
* #### `size` (optional): 페이지 크기 (기본값: 10)

### Response:

```json
{
  "success": true,
  "data": [
    {
      "id": "3",
      "userId": "user123",
      "userNickname": "재헌",
      "text": "이 쉼터 정말 깨끗하고 좋아요!",
      "rating": 5,
      "photoUrls": [
        "https://example.com/photo1.jpg",
        "https://example.com/photo2.jpg"
      ],
      "createdAt": "2025-08-18T16:19:29"
    }
  ]
}
```

---

## 🗂️ 도메인 구조

---

```
review
 ┣ controller
 ┃ ┗ ReviewController.java        # 리뷰 API 엔드포인트
 ┣ dto
 ┃ ┣ ReviewCreateRequest.java     # 리뷰 작성 요청 DTO
 ┃ ┣ ReviewCreateResponse.java    # 리뷰 작성 응답 DTO
 ┃ ┣ ReviewUpdateRequest.java     # 리뷰 수정 요청 DTO
 ┃ ┣ ReviewUpdateResponse.java    # 리뷰 수정 응답 DTO
 ┃ ┣ ReviewListRequest.java       # 리뷰 목록 조회 요청 DTO
 ┃ ┗ ReviewListResponse.java      # 리뷰 목록 조회 응답 DTO
 ┣ entity
 ┃ ┗ Review.java                  # 리뷰 엔티티
 ┣ repository
 ┃ ┗ ReviewRepository.java        # 리뷰 JPA Repository
 ┗ service
   ┗ ReviewService.java           # 리뷰 비즈니스 로직
 ┗ README.md                      # 이 파일
```

---

## 🔧 주요 기능

---

### 1. CRUD 작업

* Create: 새로운 리뷰 등록
* Read: 리뷰 전체 목록 조회 및 특정 사용자 리뷰 조회
* Update:  기존의 리뷰 수정
* Delete: 리뷰 삭제

### 2. 페이징 지원

* 목록 조회 시 페이지 단위로 데이터 제공
* 기존 페이지 크기: 10개
* 페이지 번호는 0부터 시작

### 3. 자동 타임스탬프

* createdAt: 생성 시간(자동 설정)
* updatedAt: 수정 시간 (자동 업데이트)

---

## 📜 사용 예시

---

### Postman 테스트

### 1. 리뷰 등록

```json{
  Post /api/v1/shelters/{shelterId}/reviews
  Content-Type: application/json

{
  "text": "이 쉼터 정말 깨끗하고 좋아요!",
  "rating": 5,
  "userNickname": "사용자",
  "userId": "user123",
  "photoUrls": [
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg"
  ]

}

```

### 2. 리뷰 목록 조회

```json{
  GET /api/v1/shelters/{shelterId}/reviews?page=0&size=5
  Content-Type: application/json
```

### 3. 리뷰 수정

```json{
  PUT /api/v1/reviews/{reviewId}
  Content-Type: application/json
 
  {
  "text": "수정된 리뷰 내용",
  "rating": 3,
  "photoUrls": [
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg"
  ]
}
```

### 4. 리뷰 삭제

```json{
    DELETE /api/v1/reviews/{reviewId}
```

---

## 🔒 보안 고려사항

---

* #### 현재는 인증/인가 없이 모든 API 접근 가능
* #### 향후 관리자 권한 체크 추가 필요
* #### 작성자 정보는 현재 요청에서 받지만, 실제로는 인증된 사용자 정보 사용 권장


