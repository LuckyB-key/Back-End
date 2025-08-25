package com.luckyb.global.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService implements AiService {

  @Value("${openai.api-key}")
  private String apiKey;

  @Value("${openai.model}")
  private String model;

  @Value("${openai.max-tokens}")
  private int maxTokens;

  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

  @Override
  public String generateText(String prompt) {
    try {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("model", model);
      requestBody.put("max_tokens", maxTokens);
      requestBody.put("temperature", 0.7);

      List<Map<String, String>> messages = new ArrayList<>();
      messages.add(Map.of("role", "user", "content", prompt));
      requestBody.put("messages", messages);

      String jsonBody = objectMapper.writeValueAsString(requestBody);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(OPENAI_API_URL))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + apiKey)
          .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
          .build();

      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        if (!choices.isEmpty()) {
          Map<String, Object> choice = choices.get(0);
          Map<String, Object> message = (Map<String, Object>) choice.get("message");
          return (String) message.get("content");
        }
      }

      log.error("OpenAI API 호출 실패: {}", response.body());
      return "AI 서비스에 일시적인 문제가 발생했습니다.";

    } catch (Exception e) {
      log.error("OpenAI API 호출 중 오류 발생", e);
      return "AI 서비스에 일시적인 문제가 발생했습니다.";
    }
  }

  @Override
  public List<Map<String, Object>> recommendShelters(double lat, double lng, String[] preferences,
      String category) {
    String preferencesStr = preferences != null ? String.join(", ", preferences) : "";
    String prompt = String.format(
        "위도 %.6f, 경도 %.6f 위치에서 %s 카테고리의 쉼터를 추천해주세요. " +
            "선호 시설: %s. 거리, 혼잡도, 시설 등을 고려해주세요. " +
            "JSON 형태로 응답해주세요: [{\"id\": \"shelter1\", \"name\": \"시립도서관\", \"distance\": 0.5, \"status\": \"한산함\", \"facilities\": [\"냉방기\", \"휠체어 접근\"], \"predictedCongestion\": \"낮음\"}]",
        lat, lng, category != null ? category : "일반", preferencesStr
    );

    String response = generateText(prompt);
    try {
      return objectMapper.readValue(response, List.class);
    } catch (Exception e) {
      log.error("쉼터 추천 응답 파싱 오류", e);
      // 기본 쉼터 반환
      return List.of(
          Map.of(
              "id", "shelter1",
              "name", "가까운 무더위 쉼터",
              "distance", 0.3,
              "status", "한산함",
              "facilities", List.of("냉방기", "정수기"),
              "predictedCongestion", "낮음"
          ),
          Map.of(
              "id", "shelter2",
              "name", "지역 커뮤니티 센터",
              "distance", 0.8,
              "status", "보통",
              "facilities", List.of("냉방기", "휠체어 접근", "의자"),
              "predictedCongestion", "보통"
          )
      );
    }
  }

  @Override
  public Map<String, Object> predictCongestion(String shelterId, String date, String time) {
    String prompt = String.format(
        "쉼터 ID %s에서 %s %s의 혼잡도를 예측해주세요. " +
            "JSON 형태로 응답해주세요: {\"status\": \"한산함\", \"currentOccupancy\": 10, \"predictedOccupancy\": 15, \"capacity\": 50, \"message\": \"예측 메시지\"}",
        shelterId, date, time
    );

    String response = generateText(prompt);
    try {
      return objectMapper.readValue(response, Map.class);
    } catch (Exception e) {
      log.error("혼잡도 예측 응답 파싱 오류", e);
      return Map.of(
          "status", "알 수 없음",
          "currentOccupancy", 0,
          "predictedOccupancy", 0,
          "capacity", 0,
          "message", "예측에 실패했습니다."
      );
    }
  }

  @Override
  public List<Map<String, Object>> recommendAdvertisements(double lat, double lng, String userId) {
    String prompt = String.format(
        "위도 %.6f, 경도 %.6f 위치에서 사용자 ID %s를 위한 맞춤형 광고를 추천해주세요. " +
            "현지 비즈니스, 프로모션, 이벤트 등을 포함해주세요. " +
            "JSON 형태로 응답해주세요: [{\"id\": \"ad1\", \"ad_type\": \"location_based\", \"content\": \"현재 위치 근처 맛집 할인 이벤트\", \"businessName\": \"맛집체인\", \"image\": \"https://example.com/ad1.jpg\"}]",
        lat, lng, userId != null ? userId : "anonymous"
    );

    String response = generateText(prompt);
    try {
      return objectMapper.readValue(response, List.class);
    } catch (Exception e) {
      log.error("광고 추천 응답 파싱 오류", e);
      // 기본 광고 반환
      return List.of(
          Map.of(
              "id", "ad1",
              "ad_type", "location_based",
              "content", "현재 위치 근처 무더위 쉼터 안내",
              "businessName", "Lucky B-Key",
              "image", "https://example.com/shelter-ad.jpg"
          ),
          Map.of(
              "id", "ad2",
              "ad_type", "banner",
              "content", "무더위 대비 건강 관리 팁",
              "businessName", "건강관리센터",
              "image", "https://example.com/health-ad.jpg"
          )
      );
    }
  }

  @Override
  public List<Map<String, Object>> recommendNotifications(double latitude, double longitude) {
    String prompt = String.format(
        "위도 %.6f, 경도 %.6f 위치에서 사용자를 위한 맞춤형 알림을 추천해주세요. " +
            "무더위 쉼터 안내, 날씨 알림, 건강 주의사항 등을 포함해주세요. " +
            "JSON 형태로 응답해주세요: [{\"notificationId\": 101, \"title\": \"가까운 무더위 쉼터 안내\", \"content\": \"현재 위치에서 500m 거리에 혼잡도가 낮은 쉼터가 있습니다.\", \"type\": \"SHELTER_ALERT\"}, {\"notificationId\": 102, \"title\": \"폭염 주의 알림\", \"content\": \"오늘 오후 2시~5시 기온이 35도 이상 예상됩니다. 외출 시 주의하세요.\", \"type\": \"WEATHER_ALERT\"}]",
        latitude, longitude
    );

    String response = generateText(prompt);
    try {
      return objectMapper.readValue(response, List.class);
    } catch (Exception e) {
      log.error("알림 추천 응답 파싱 오류", e);
      // 기본 알림 반환
      return List.of(
          Map.of(
              "notificationId", 101,
              "title", "무더위 쉼터 안내",
              "content", "현재 위치 주변의 무더위 쉼터를 확인해보세요.",
              "type", "SHELTER_ALERT"
          ),
          Map.of(
              "notificationId", 102,
              "title", "건강 주의사항",
              "content", "무더위 시기에는 충분한 수분 섭취와 휴식을 취하세요.",
              "type", "HEALTH_ALERT"
          )
      );
    }
  }


} 