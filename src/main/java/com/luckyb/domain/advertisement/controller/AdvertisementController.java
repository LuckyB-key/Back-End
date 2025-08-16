package com.luckyb.domain.advertisement.controller;

import com.luckyb.domain.advertisement.dto.AdvertisementRequest;
import com.luckyb.domain.advertisement.dto.AdvertisementResponse;
import com.luckyb.domain.advertisement.service.AdvertisementService;
import com.luckyb.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
