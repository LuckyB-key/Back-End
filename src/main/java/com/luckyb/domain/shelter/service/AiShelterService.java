package com.luckyb.domain.shelter.service;

import com.luckyb.domain.shelter.dto.CongestionRequest;
import com.luckyb.domain.shelter.dto.CongestionResponse;
import com.luckyb.domain.shelter.dto.ShelterRecommendationRequest;
import com.luckyb.domain.shelter.dto.ShelterRecommendationResponse;
import com.luckyb.global.ai.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiShelterService {

  private final AiService aiService;

  public List<ShelterRecommendationResponse> getShelterRecommendations(
      ShelterRecommendationRequest request) {
    try {
      log.info("AI 쉼터 추천 요청: lat={}, lng={}, preferences={}, category={}",
          request.getLat(), request.getLng(), request.getPreferences(), request.getCategory());

      // List<String>을 String[]로 변환
      String[] preferencesArray = request.getPreferences() != null ?
          request.getPreferences().toArray(new String[0]) : null;

      List<Map<String, Object>> aiRecommendations = aiService.recommendShelters(
          request.getLat(),
          request.getLng(),
          preferencesArray,
          request.getCategory()
      );

      if (aiRecommendations == null || aiRecommendations.isEmpty()) {
        log.warn("AI 추천 결과가 비어있습니다. 기본 추천을 반환합니다.");
        return List.of(ShelterRecommendationResponse.builder()
            .id("default-id")
            .name("AI 추천 쉼터")
            .distance(0.5)
            .status("한산함")
            .facilities(List.of("냉방기", "휠체어 접근"))
            .predictedCongestion("한산함")
            .build());
      }

      return aiRecommendations.stream()
          .map(this::mapToShelterRecommendationResponse)
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("AI 쉼터 추천 처리 중 오류 발생: {}", e.getMessage(), e);
      // 기본 추천 반환
      return List.of(ShelterRecommendationResponse.builder()
          .id("default-id")
          .name("AI 추천 쉼터")
          .distance(0.5)
          .status("한산함")
          .facilities(List.of("냉방기", "휠체어 접근"))
          .predictedCongestion("한산함")
          .build());
    }
  }

  public CongestionResponse predictCongestion(String shelterId, CongestionRequest request) {
    log.info("AI 혼잡도 예측 요청: shelterId={}, date={}, time={}",
        shelterId, request.getDate(), request.getTime());

    Map<String, Object> aiPrediction = aiService.predictCongestion(
        shelterId,
        request.getDate(),
        request.getTime()
    );

    return mapToCongestionResponse(aiPrediction);
  }

  private ShelterRecommendationResponse mapToShelterRecommendationResponse(
      Map<String, Object> aiData) {
    try {
      return ShelterRecommendationResponse.builder()
          .id((String) aiData.get("id"))
          .name((String) aiData.get("name"))
          .distance(aiData.get("distance") != null ? ((Number) aiData.get("distance")).doubleValue()
              : 0.0)
          .status((String) aiData.get("status"))
          .facilities(aiData.get("facilities") != null ? (List<String>) aiData.get("facilities")
              : List.of())
          .predictedCongestion((String) aiData.get("predictedCongestion"))
          .build();
    } catch (Exception e) {
      log.error("AI 응답 데이터 매핑 오류: {}", e.getMessage(), e);
      // 기본 응답 반환
      return ShelterRecommendationResponse.builder()
          .id("default-id")
          .name("AI 추천 쉼터")
          .distance(0.5)
          .status("한산함")
          .facilities(List.of("냉방기", "휠체어 접근"))
          .predictedCongestion("한산함")
          .build();
    }
  }

  private CongestionResponse mapToCongestionResponse(Map<String, Object> aiData) {
    try {
      return CongestionResponse.builder()
          .status((String) aiData.get("status"))
          .currentOccupancy(aiData.get("currentOccupancy") != null ? ((Number) aiData.get(
              "currentOccupancy")).intValue() : 0)
          .predictedOccupancy(aiData.get("predictedOccupancy") != null ? ((Number) aiData.get(
              "predictedOccupancy")).intValue() : 0)
          .capacity(
              aiData.get("capacity") != null ? ((Number) aiData.get("capacity")).intValue() : 0)
          .message((String) aiData.get("message"))
          .build();
    } catch (Exception e) {
      log.error("AI 혼잡도 예측 데이터 매핑 오류: {}", e.getMessage(), e);
      // 기본 응답 반환
      return CongestionResponse.builder()
          .status("알 수 없음")
          .currentOccupancy(0)
          .predictedOccupancy(0)
          .capacity(0)
          .message("예측에 실패했습니다.")
          .build();
    }
  }
} 