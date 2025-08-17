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
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
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
    public List<Map<String, Object>> recommendShelters(double lat, double lng, List<String> preferences, String category) {
        String prompt = String.format(
            "위도 %.6f, 경도 %.6f 위치에서 %s 카테고리의 쉼터를 추천해주세요. " +
            "선호 시설: %s. " +
            "JSON 형태로 응답해주세요: [{\"id\": \"shelter_id\", \"name\": \"쉼터명\", \"distance\": 0.5, \"status\": \"한산함\", \"facilities\": [\"냉방기\"], \"predictedCongestion\": \"한산함\"}]",
            lat, lng, category, String.join(", ", preferences)
        );
        
        String response = generateText(prompt);
        try {
            return objectMapper.readValue(response, List.class);
        } catch (Exception e) {
            log.error("쉼터 추천 응답 파싱 오류", e);
            return new ArrayList<>();
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
    

} 