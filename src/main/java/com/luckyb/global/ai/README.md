# AI 서비스 (AI Service)

OpenAI API를 활용한 AI 기반 추천 및 예측 서비스를 제공하는 모듈입니다.

## 🎯 주요 기능

- **OpenAI API 연동**: GPT 모델을 활용한 자연어 처리
- **쉼터 추천**: 위치 기반 AI 쉼터 추천
- **혼잡도 예측**: AI 기반 혼잡도 예측

## 🏗️ 구조

```
global/ai/
├── AiService.java           # AI 서비스 인터페이스
├── OpenAiService.java       # OpenAI API 구현체
└── README.md               # 이 파일
```

## 📋 API 명세

### AiService 인터페이스

```java
public interface AiService {
    
    /**
     * OpenAI API를 사용하여 텍스트 생성
     */
    String generateText(String prompt);
    
    /**
     * 쉼터 추천을 위한 AI 분석
     */
    List<Map<String, Object>> recommendShelters(double lat, double lng, List<String> preferences, String category);
    
    /**
     * 혼잡도 예측을 위한 AI 분석
     */
    Map<String, Object> predictCongestion(String shelterId, String date, String time);
    

}
```

## 🔧 설정

### OpenAI API 설정

`application.yml`에서 OpenAI 설정:

```yaml
openai:
  api-key: ${OPENAI_API_KEY:your-openai-api-key-here}
  model: gpt-3.5-turbo
  max-tokens: 1000
```

### 환경변수 설정

```bash
export OPENAI_API_KEY=your_openai_api_key_here
```

## 🤖 AI 프롬프트 예시

### 쉼터 추천 프롬프트

```
위도 37.5665, 경도 126.9780 위치에서 노인 카테고리의 쉼터를 추천해주세요. 
선호 시설: 냉방기, 휠체어 접근. 
JSON 형태로 응답해주세요: [{"id": "shelter_id", "name": "쉼터명", "distance": 0.5, "status": "한산함", "facilities": ["냉방기"], "predictedCongestion": "한산함"}]
```

### 혼잡도 예측 프롬프트

```
쉼터 ID shelter_001에서 2024-01-15 14:00의 혼잡도를 예측해주세요. 
JSON 형태로 응답해주세요: {"status": "한산함", "currentOccupancy": 10, "predictedOccupancy": 15, "capacity": 50, "message": "예측 메시지"}
```



## 🔒 보안 고려사항

- **API 키 보안**: OpenAI API 키는 환경변수로 관리
- **요청 제한**: API 호출 횟수 제한 고려
- **에러 처리**: API 실패 시 적절한 폴백 응답
- **데이터 검증**: AI 응답 데이터 유효성 검증

## 🚀 성능 최적화

### 캐싱 전략

- **응답 캐싱**: 동일한 요청에 대한 응답 캐싱
- **TTL 설정**: 캐시 유효기간 설정
- **캐시 무효화**: 데이터 변경 시 캐시 무효화

### 비동기 처리

- **비동기 호출**: OpenAI API 비동기 호출
- **타임아웃 설정**: API 호출 타임아웃 설정
- **재시도 로직**: 실패 시 재시도 메커니즘

## 🧪 테스트

### 단위 테스트

```java
@SpringBootTest
class OpenAiServiceTest {
    
    @Autowired
    private AiService aiService;
    
    @Test
    void testGenerateText() {
        String response = aiService.generateText("테스트 프롬프트");
        assertNotNull(response);
    }
}
```

### 통합 테스트

```bash
# 쉼터 추천 테스트
curl -X GET "http://localhost:8080/api/v1/shelters/recommendations?lat=37.5665&lng=126.9780&category=노인"

# 혼잡도 예측 테스트
curl -X GET "http://localhost:8080/api/v1/shelters/shelter_001/congestion?date=2024-01-15&time=14:00"


```

## 🔄 확장 계획

- **다중 AI 모델**: GPT-4, Claude 등 다양한 모델 지원
- **로컬 AI 모델**: 오프라인 AI 모델 연동
- **AI 모델 최적화**: 프롬프트 엔지니어링 개선
- **AI 성능 모니터링**: 응답 시간, 정확도 측정
- **AI 학습**: 사용자 피드백 기반 모델 개선 