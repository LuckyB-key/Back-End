package com.luckyb.domain.advertisement.service;

import static com.luckyb.global.exception.ErrorCode.ADVERTISEMENT_NOT_FOUND;

import com.luckyb.domain.advertisement.dto.AdvertisementRequest;
import com.luckyb.domain.advertisement.dto.AdvertisementResponse;
import com.luckyb.domain.advertisement.dto.AdListRequest;
import com.luckyb.domain.advertisement.dto.AdListResponse;
import com.luckyb.domain.advertisement.entity.Advertisement;
import com.luckyb.domain.advertisement.repository.AdvertisementRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.repository.UserRepository;
import com.luckyb.global.ai.AiService;
import com.luckyb.global.exception.AdvertisementNotFoundException;
import com.luckyb.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertisementService {

  private final AdvertisementRepository advertisementRepository;
  private final UserRepository userRepository;
  private final AiService aiService;

  private User getDefaultUser() {
    return userRepository.findAll()
        .stream()
        .findFirst()
        .orElseGet(() -> userRepository.save(User.createDefaultUser()));
  }

  public AdvertisementResponse createAdvertisement(AdvertisementRequest request) {
    User user = getDefaultUser();

    Advertisement advertisement = Advertisement.builder()
        .user(user)
        .title(request.getTitle())
        .content(request.getContent())
        .build();

    advertisement = advertisementRepository.save(advertisement);

    return AdvertisementResponse.from(advertisement);
  }

  public Page<AdvertisementResponse> getAllAdvertisements(Pageable pageable) {
    Page<Advertisement> advertisements = advertisementRepository.findAll(pageable);
    if (advertisements.isEmpty()) {
      return Page.empty(pageable);
    }
    return advertisements.map(AdvertisementResponse::from);
  }

  public AdvertisementResponse getAdvertisement(Long id) {
    Advertisement advertisement = advertisementRepository.findById(id)
        .orElseThrow(() -> new AdvertisementNotFoundException(ADVERTISEMENT_NOT_FOUND));
    return AdvertisementResponse.from(advertisement);
  }

  @Transactional
  public AdvertisementResponse updateAdvertisement(Long id, AdvertisementRequest request) {
    User user = getDefaultUser();

    Advertisement advertisement = advertisementRepository.findById(id)
        .orElseThrow(() -> new AdvertisementNotFoundException(ADVERTISEMENT_NOT_FOUND));

    // 광고 작성자와 기본 유저가 다르면 수정 불가 (대회용 기본 유저라 항상 허용 가능)
    if (!advertisement.getUser().equals(user)) {
      throw new AdvertisementNotFoundException(ErrorCode.ACCESS_DENIED);
    }

    Advertisement updated = updateAdvertisementFields(advertisement, request);
    advertisementRepository.save(updated);

    return AdvertisementResponse.from(updated);
  }

  @Transactional
  public void deleteAdvertisement(Long id) {
    User user = getDefaultUser();

    Advertisement advertisement = advertisementRepository.findById(id)
        .orElseThrow(() -> new AdvertisementNotFoundException(ADVERTISEMENT_NOT_FOUND));

    if (!advertisement.getUser().equals(user)) {
      throw new AdvertisementNotFoundException(ErrorCode.ACCESS_DENIED);
    }

    advertisementRepository.delete(advertisement);
  }

  private Advertisement updateAdvertisementFields(Advertisement advertisement,
      AdvertisementRequest request) {
    String updatedTitle = (request.getTitle() != null && !request.getTitle().trim().isEmpty())
        ? request.getTitle() : advertisement.getTitle();
    String updatedContent = (request.getContent() != null && !request.getContent().trim().isEmpty())
        ? request.getContent() : advertisement.getContent();

    return Advertisement.builder()
        .id(advertisement.getId())
        .user(advertisement.getUser())
        .title(updatedTitle)
        .content(updatedContent)
        .image(advertisement.getImage())
        .createdAt(advertisement.getCreatedAt())
        .updatedAt(advertisement.getUpdatedAt())
        .build();
  }
  
  /**
   * AI 맞춤 광고 추천
   */
  public List<AdListResponse> getAiAdvertisements(AdListRequest request) {
    try {
      log.info("AI 광고 추천 요청: lat={}, lng={}, userId={}", 
              request.getLat(), request.getLng(), request.getUserId());
      
      List<Map<String, Object>> aiRecommendations = aiService.recommendAdvertisements(
              request.getLat(), 
              request.getLng(), 
              request.getUserId()
      );
      
      if (aiRecommendations == null || aiRecommendations.isEmpty()) {
        log.warn("AI 광고 추천 결과가 비어있습니다. 기본 광고를 반환합니다.");
        return List.of(AdListResponse.builder()
                .id("default-ad")
                .adType("banner")
                .content("무더위 쉼터 안내")
                .businessName("서울시청")
                .image("https://example.com/default-ad.jpg")
                .build());
      }
      
      return aiRecommendations.stream()
              .map(this::mapToAdListResponse)
              .collect(Collectors.toList());
              
    } catch (Exception e) {
      log.error("AI 광고 추천 처리 중 오류 발생: {}", e.getMessage(), e);
      return List.of(AdListResponse.builder()
              .id("error-ad")
              .adType("banner")
              .content("서비스 일시 중단")
              .businessName("시스템")
              .image("https://example.com/error-ad.jpg")
              .build());
    }
  }
  
  private AdListResponse mapToAdListResponse(Map<String, Object> aiAd) {
    return AdListResponse.builder()
            .id((String) aiAd.getOrDefault("id", "unknown"))
            .adType((String) aiAd.getOrDefault("ad_type", "banner"))
            .content((String) aiAd.getOrDefault("content", "광고 내용"))
            .businessName((String) aiAd.getOrDefault("businessName", "광고주"))
            .image((String) aiAd.getOrDefault("image", "https://example.com/default.jpg"))
            .build();
  }
}
