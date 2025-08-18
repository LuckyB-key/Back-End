package com.luckyb.domain.advertisement.controller;

import com.luckyb.domain.advertisement.dto.AdvertisementRequest;
import com.luckyb.domain.advertisement.dto.AdvertisementResponse;
import com.luckyb.domain.advertisement.dto.AdListRequest;
import com.luckyb.domain.advertisement.dto.AdListResponse;
import com.luckyb.domain.advertisement.service.AdvertisementService;
import com.luckyb.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {

  private final AdvertisementService advertisementService;

  @PostMapping
  public ResponseEntity<AdvertisementResponse> createAdvertisement(
      @RequestBody AdvertisementRequest request
  ) {
    AdvertisementResponse response = advertisementService.createAdvertisement(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AdvertisementResponse> updateAdvertisement(
      @PathVariable Long id,
      @RequestBody AdvertisementRequest request
  ) {
    AdvertisementResponse response = advertisementService.updateAdvertisement(id, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AdvertisementResponse> getAdvertisement(@PathVariable Long id) {
    AdvertisementResponse response = advertisementService.getAdvertisement(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<AdvertisementResponse>> getAllAdvertisements(Pageable pageable) {
    Page<AdvertisementResponse> responses = advertisementService.getAllAdvertisements(pageable);
    return ResponseEntity.ok(responses);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteAdvertisement(@PathVariable Long id) {
    advertisementService.deleteAdvertisement(id);
    return ResponseEntity.ok(ApiResponse.success("광고가 정상적으로 삭제되었습니다."));
  }
  
  /**
   * AI 맞춤 광고 추천
   */
  @GetMapping("/ai-recommendations")
  public ResponseEntity<ApiResponse<List<AdListResponse>>> getAiAdvertisements(
          @RequestParam Double lat,
          @RequestParam Double lng,
          @RequestParam(required = false) String userId
  ) {
    AdListRequest request = new AdListRequest();
    request.setLat(lat);
    request.setLng(lng);
    request.setUserId(userId);
    
    List<AdListResponse> advertisements = advertisementService.getAiAdvertisements(request);
    log.info("AI 맞춤 광고 추천 완료: {}개 광고", advertisements.size());
    
    return ResponseEntity.ok(ApiResponse.success(advertisements));
  }
}
