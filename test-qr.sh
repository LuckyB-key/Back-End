#!/bin/bash

# QR 코드 테스트 스크립트
echo "=== QR 코드 테스트 시작 ==="

# 애플리케이션이 실행 중인지 확인
if ! curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "❌ 애플리케이션이 실행되지 않았습니다. 먼저 애플리케이션을 시작해주세요."
    echo "   ./gradlew bootRun"
    exit 1
fi

echo "✅ 애플리케이션이 실행 중입니다."

# 테스트용 대피소 ID
SHELTER_ID="test-shelter-$(date +%s)"

echo "📱 테스트용 대피소 ID: $SHELTER_ID"

# 1. QR 코드 생성 테스트
echo "1️⃣ QR 코드 생성 테스트..."
if curl -s -o "qr-code-$SHELTER_ID.png" "http://localhost:8080/api/v1/qr/generate/$SHELTER_ID"; then
    echo "✅ QR 코드가 성공적으로 생성되었습니다: qr-code-$SHELTER_ID.png"
else
    echo "❌ QR 코드 생성에 실패했습니다."
    exit 1
fi

# 2. QR 코드 디코딩 테스트
echo "2️⃣ QR 코드 디코딩 테스트..."
QR_CONTENT="luckyb://checkin/$SHELTER_ID"
DECODED_ID=$(curl -s -X POST "http://localhost:8080/api/v1/qr/decode" \
    -H "Content-Type: text/plain" \
    -d "$QR_CONTENT")

if [ "$DECODED_ID" = "$SHELTER_ID" ]; then
    echo "✅ QR 코드 디코딩이 성공했습니다: $DECODED_ID"
else
    echo "❌ QR 코드 디코딩에 실패했습니다. 예상: $SHELTER_ID, 실제: $DECODED_ID"
    exit 1
fi

# 3. 잘못된 QR 코드 내용 테스트
echo "3️⃣ 잘못된 QR 코드 내용 테스트..."
INVALID_CONTENT="invalid-qr-content"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://localhost:8080/api/v1/qr/decode" \
    -H "Content-Type: text/plain" \
    -d "$INVALID_CONTENT")

if [ "$HTTP_STATUS" = "400" ]; then
    echo "✅ 잘못된 QR 코드 내용에 대한 에러 처리가 정상입니다."
else
    echo "❌ 잘못된 QR 코드 내용에 대한 에러 처리가 실패했습니다. HTTP 상태: $HTTP_STATUS"
    exit 1
fi

# 4. 체크인 API 테스트 (JWT 토큰이 필요한 경우 주석 처리)
echo "4️⃣ 체크인 API 테스트 (JWT 토큰 필요 시 주석 처리됨)..."
# curl -s -X POST "http://localhost:8080/api/v1/shelters/$SHELTER_ID/checkins" \
#     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
#     -H "Content-Type: application/json"

echo "📋 체크인 API 테스트는 JWT 토큰이 필요하므로 수동으로 테스트해주세요:"
echo "   curl -X POST \"http://localhost:8080/api/v1/shelters/$SHELTER_ID/checkins\" \\"
echo "        -H \"Authorization: Bearer YOUR_JWT_TOKEN\" \\"
echo "        -H \"Content-Type: application/json\""

echo ""
echo "=== QR 코드 테스트 완료 ==="
echo "📁 생성된 파일:"
echo "   - qr-code-$SHELTER_ID.png (QR 코드 이미지)"
echo ""
echo "🔍 QR 코드 내용: $QR_CONTENT"
echo "📱 스마트폰으로 QR 코드를 스캔하여 테스트해보세요!" 