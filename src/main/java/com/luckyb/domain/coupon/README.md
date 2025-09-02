# 쿠폰 도메인 (Coupon Domain)

쿠폰 등록, 조회, 사용 처리를 담당합니다.

## 📋 API 명세

### 쿠폰 등록

비즈니스 사용자가 새로운 쿠폰을 등록합니다.

**Endpoint:** `POST /api/v1/coupons`

**요청:**

- **Method**: POST
- **Headers**: `Authorization: Bearer {JWT_TOKEN}` (비즈니스 사용자 권한 필요)
- **Content-Type**: application/json

**요청 본문:**

```json
{
  "title": "여름 할인 쿠폰",
  "description": "모든 상품 20% 할인",
  "expiryDate": "2024-08-31T23:59:59"
}
```

**응답:**

```json
{
  "success": true,
  "data": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "title": "여름 할인 쿠폰",
    "businessId": "business-user-id"
  }
}
```

### 쿠폰 목록 조회

모든 활성 쿠폰 목록을 페이징하여 조회합니다.

**Endpoint:** `GET /api/v1/coupons`

**요청:**

- **Method**: GET
- **Query Parameters**:
    - `page`: 페이지 번호 (선택, 기본값: 0)
    - `size`: 페이지 크기 (선택, 기본값: 10, 최대: 100)

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "title": "여름 할인 쿠폰",
      "description": "모든 상품 20% 할인",
      "expiryDate": "2024-08-31T23:59:59",
      "businessName": "카페 아메리카노"
    }
  ]
}
```

### 쿠폰 상세 조회

특정 쿠폰의 상세 정보를 조회합니다.

**Endpoint:** `GET /api/v1/coupons/{couponId}`

**응답:**

```json
{
  "success": true,
  "data": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "title": "여름 할인 쿠폰",
    "description": "모든 상품 20% 할인",
    "expiryDate": "2024-08-31T23:59:59",
    "businessId": "business-user-id",
    "businessName": "카페 아메리카노"
  }
}
```

### 쿠폰 발급

사용자가 쿠폰을 발급받습니다.

**Endpoint:** `POST /api/v1/coupons/{couponId}/issue`

**요청:**

- **Method**: POST
- **Headers**: `Authorization: Bearer {JWT_TOKEN}`
- **Content-Type**: application/json

**응답:**

```json
{
  "success": true,
  "data": {
    "userCouponId": "user-coupon-id",
    "couponId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "title": "여름 할인 쿠폰",
    "description": "모든 상품 20% 할인",
    "status": "active",
    "issuedAt": "2024-07-15T14:30:00",
    "expiryDate": "2024-08-31T23:59:59"
  }
}
```

### 쿠폰 사용 처리

사용자가 발급받은 쿠폰을 사용합니다.

**Endpoint:** `POST /api/v1/coupons/{couponId}/use`

**요청:**

- **Method**: POST
- **Headers**: `Authorization: Bearer {JWT_TOKEN}`
- **Content-Type**: application/json

**요청 본문:**

```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "title": "여름 할인 쿠폰",
  "description": "모든 상품 20% 할인",
  "usedAt": "2024-07-15T14:30:00"
}
```

**응답:**

```json
{
  "success": true,
  "data": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "title": "여름 할인 쿠폰",
    "description": "모든 상품 20% 할인",
    "status": "used",
    "usedAt": "2024-07-15T14:30:00",
    "userId": "user-id"
  }
}
```

### 추가 API

**내가 발행한 쿠폰 목록 조회 (비즈니스 사용자용):**

- `GET /api/v1/coupons/my-business`

**내 쿠폰 목록 조회:**

- `GET /api/v1/coupons/my-coupons`

**내가 사용한 쿠폰 목록 조회:**

- `GET /api/v1/coupons/my-used`

## 🏗️ 구조

### Entity

- `Coupon.java` - 쿠폰 엔티티

### Controller

- `CouponController.java` - 쿠폰 관련 REST API 엔드포인트

### Service

- `CouponService.java` - 쿠폰 비즈니스 로직

### Repository

- `CouponRepository.java` - 쿠폰 데이터 액세스

### DTO

- `CouponCreateRequest.java` - 쿠폰 등록 요청
- `CouponCreateResponse.java` - 쿠폰 등록 응답
- `CouponListRequest.java` - 쿠폰 목록 조회 요청
- `CouponListResponse.java` - 쿠폰 목록 조회 응답
- `CouponDetailResponse.java` - 쿠폰 상세 조회 응답
- `CouponUseRequest.java` - 쿠폰 사용 요청
- `CouponUseResponse.java` - 쿠폰 사용 응답

## 📊 데이터베이스 스키마

### coupons 테이블

```sql
CREATE TABLE coupons
(
    coupon_id        VARCHAR(255) PRIMARY KEY,
    title            VARCHAR(100) NOT NULL,
    description      VARCHAR(500),
    expiry_date      DATETIME     NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    used_at          DATETIME,
    business_user_id VARCHAR(255) NOT NULL,
    used_by_user_id  VARCHAR(255),
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL,
    FOREIGN KEY (business_user_id) REFERENCES users (user_id),
    FOREIGN KEY (used_by_user_id) REFERENCES users (user_id)
);
```

### user_coupons 테이블

```sql
CREATE TABLE user_coupons
(
    user_coupon_id VARCHAR(255) PRIMARY KEY,
    user_id        VARCHAR(255) NOT NULL,
    coupon_id      VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    issued_at      DATETIME     NOT NULL,
    used_at        DATETIME,
    created_at     DATETIME     NOT NULL,
    updated_at     DATETIME     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id)
);
```

## 🔒 권한 관리

- **쿠폰 등록**: 비즈니스 사용자(BUSINESS) 권한 필요
- **쿠폰 조회**: 모든 사용자 가능
- **쿠폰 사용**: 로그인된 사용자 권한 필요

## 📝 비즈니스 규칙

1. **쿠폰 등록**: 비즈니스 사용자만 쿠폰을 등록할 수 있음
2. **쿠폰 발급**: 사용자는 쿠폰을 발급받아야 사용 가능
3. **중복 발급 방지**: 같은 사용자가 같은 쿠폰을 중복 발급받을 수 없음
4. **쿠폰 사용**: 발급받은 쿠폰만 사용 가능하며, 한 번 사용된 쿠폰은 재사용 불가
5. **만료 처리**: 만료일이 지난 쿠폰은 발급 및 사용 불가
6. **상태 관리**:
    - 쿠폰 상태: ACTIVE, USED, EXPIRED, CANCELLED
    - 사용자 쿠폰 상태: ACTIVE, USED, EXPIRED 