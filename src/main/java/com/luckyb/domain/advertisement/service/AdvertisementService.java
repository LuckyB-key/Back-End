package com.luckyb.domain.advertisement.service;

import static com.luckyb.global.exception.ErrorCode.ADVERTISEMENT_NOT_FOUND;

import com.luckyb.domain.advertisement.dto.AdvertisementRequest;
import com.luckyb.domain.advertisement.dto.AdvertisementResponse;
import com.luckyb.domain.advertisement.entity.Advertisement;
import com.luckyb.domain.advertisement.repository.AdvertisementRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.repository.UserRepository;
import com.luckyb.global.exception.AdvertisementNotFoundException;
import com.luckyb.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

  private final AdvertisementRepository advertisementRepository;
  private final UserRepository userRepository;

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
}
