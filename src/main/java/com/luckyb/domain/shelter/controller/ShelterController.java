package com.luckyb.domain.shelter.controller;

import com.luckyb.domain.shelter.dto.*;
import com.luckyb.domain.shelter.service.ShelterService;
import com.luckyb.domain.shelter.service.AiShelterService;
import com.luckyb.global.ai.AiService;
import com.luckyb.global.common.ApiResponse;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/shelters")
@RequiredArgsConstructor
public class ShelterController {

  private final ShelterService shelterService;
  private final AiShelterService aiShelterService;
  private final JwtTokenProvider jwtTokenProvider;
  private final AiService aiService;

  /**
   * 쉼터 목록 조회 (위치 기반)
   */
  @GetMapping
  public ApiResponse<List<ShelterListResponse>> getShelterList(
      @RequestParam Double lat,
      @RequestParam Double lng,
      @RequestParam(required = false) Double distance,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) String facilities
  ) {
    ShelterListRequest request = new ShelterListRequest();
    request.setLat(lat);
    request.setLng(lng);
    request.setDistance(distance);
    request.setType(type);
    request.setFacilities(facilities);

    List<ShelterListResponse> shelters = shelterService.getShelterList(request);
    return ApiResponse.success(shelters);
  }

  /**
   * 쉼터 상세 조회
   */
  @GetMapping("/{shelterId}")
  public ApiResponse<ShelterDetailResponse> getShelterDetail(
      @PathVariable String shelterId
  ) {
    ShelterDetailResponse shelter = shelterService.getShelterDetail(shelterId);
    return ApiResponse.success(shelter);
  }

  /**
   * 쉼터 등록
   */
  @PostMapping
  public ApiResponse<ShelterCreateResponse> createShelter(
      @RequestBody ShelterCreateRequest request,
      @RequestHeader("Authorization") String authorization
  ) {
    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    String userId = jwtTokenProvider.getUserIdFromToken(token);
    ShelterCreateResponse response = shelterService.createShelter(request, userId);
    log.info("새 쉼터가 등록되었습니다. shelterId: {}", response.getId());
    return ApiResponse.success(response);
  }

  /**
   * 쉼터 수정
   */
  @PutMapping("/{shelterId}")
  public ApiResponse<ShelterUpdateResponse> updateShelter(
      @PathVariable String shelterId,
      @RequestBody ShelterUpdateRequest request
  ) {
    ShelterUpdateResponse response = shelterService.updateShelter(shelterId, request);
    log.info("쉼터 정보가 수정되었습니다. shelterId: {}", shelterId);
    return ApiResponse.success(response);
  }

  /**
   * 쉼터 삭제
   */
  @DeleteMapping("/{shelterId}")
  public ApiResponse<ShelterDeleteResponse> deleteShelter(
      @PathVariable String shelterId
  ) {
    ShelterDeleteResponse response = shelterService.deleteShelter(shelterId);
    log.info("쉼터가 삭제되었습니다. shelterId: {}", shelterId);
    return ApiResponse.success(response);
  }

  /**
   * AI 쉼터 추천
   */
  @GetMapping("/recommendations")
  public ApiResponse<List<ShelterRecommendationResponse>> getRecommendedShelters(
      @RequestParam Double lat,
      @RequestParam Double lng,
      @RequestParam(required = false) List<String> preferences,
      @RequestParam(required = false) String category
  ) {
    ShelterRecommendationRequest request = new ShelterRecommendationRequest();
    request.setLat(lat);
    request.setLng(lng);
    request.setPreferences(preferences);
    request.setCategory(category);

    List<ShelterRecommendationResponse> recommendations = aiShelterService.getShelterRecommendations(
        request);
    return ApiResponse.success(recommendations);
  }

  /**
   * AI 혼잡도 예측
   */
  @GetMapping("/{shelterId}/congestion")
  public ApiResponse<CongestionResponse> getCongestionPrediction(
      @PathVariable String shelterId,
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String time
  ) {
    CongestionRequest request = new CongestionRequest();
    request.setDate(date);
    request.setTime(time);

    CongestionResponse response = aiShelterService.predictCongestion(shelterId, request);
    return ApiResponse.success(response);
  }

  /**
   * AI 맞춤형 알림 추천
   */
  @GetMapping("/notifications/ai-recommendations")
  public ApiResponse<List<Map<String, Object>>> getAiNotificationRecommendations(
      @RequestParam Double latitude,
      @RequestParam Double longitude
  ) {
    List<Map<String, Object>> recommendations = aiService.recommendNotifications(latitude,
        longitude);
    return ApiResponse.success(recommendations);
  }

  @PutMapping("/{shelterId}/like")
  public ApiResponse<String> toggleLike(
      @PathVariable String shelterId,
      @RequestHeader("Authorization") String authorization
  ) {
    // JWT 토큰에서 사용자 ID 추출
    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    String userId = jwtTokenProvider.getUserIdFromToken(token);

    shelterService.toggleLike(shelterId, userId);
    return ApiResponse.success("좋아요 토글 완료");
  }

  @GetMapping("/{shelterId}/likes")
  public ApiResponse<List<String>> getShelterLikes(
      @PathVariable String shelterId
  ) {
    List<String> nicknames = shelterService.getShelterLikeUsers(shelterId);
    return ApiResponse.success(nicknames);
  }

}