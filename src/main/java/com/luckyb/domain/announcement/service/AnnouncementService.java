package com.luckyb.domain.announcement.service;

import com.luckyb.domain.announcement.dto.*;
import com.luckyb.domain.announcement.entity.Announcement;
import com.luckyb.domain.announcement.repository.AnnouncementRepository;
import com.luckyb.domain.notification.service.NotificationService;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.repository.UserRepository;
import com.luckyb.global.exception.AnnouncementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementService {

  private final NotificationService notificationService;
  private final AnnouncementRepository announcementRepository;
  private final UserRepository userRepository;

  /**
   * 공지사항 등록
   */
  @Transactional
  public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest request) {
    Announcement announcement = Announcement.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .imageUrl(request.getImageUrl())
        .author(request.getAuthor())
        .build();

    Announcement savedAnnouncement = announcementRepository.save(announcement);

    List<User> allUsers = userRepository.findAll();
    notificationService.announcementNotification(announcement.getTitle(), allUsers);

    return AnnouncementCreateResponse.builder()
        .announcementId(savedAnnouncement.getAnnouncementId())
        .title(savedAnnouncement.getTitle())
        .content(savedAnnouncement.getContent())
        .imageUrl(savedAnnouncement.getImageUrl())
        .author(savedAnnouncement.getAuthor())
        .createdAt(savedAnnouncement.getCreatedAt())
        .updatedAt(savedAnnouncement.getUpdatedAt())
        .build();
  }

  /**
   * 공지사항 수정
   */
  @Transactional
  public AnnouncementUpdateResponse updateAnnouncement(String announcementId,
      AnnouncementUpdateRequest request) {
    Announcement announcement = announcementRepository.findById(announcementId)
        .orElseThrow(
            () -> new AnnouncementNotFoundException("공지사항을 찾을 수 없습니다. ID: " + announcementId));

    announcement.updateAnnouncement(request.getTitle(), request.getContent(),
        request.getImageUrl());

    return AnnouncementUpdateResponse.builder()
        .announcementId(announcement.getAnnouncementId())
        .title(announcement.getTitle())
        .content(announcement.getContent())
        .imageUrl(announcement.getImageUrl())
        .author(announcement.getAuthor())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();
  }

  /**
   * 공지사항 목록 조회 (페이징)
   */
  public AnnouncementListResponse getAnnouncementList(AnnouncementListRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Announcement> announcementPage = announcementRepository.findAll(pageable);

    List<AnnouncementListResponse.AnnouncementItem> items = announcementPage.getContent().stream()
        .map(announcement -> AnnouncementListResponse.AnnouncementItem.builder()
            .announcementId(announcement.getAnnouncementId())
            .title(announcement.getTitle())
            .content(announcement.getContent())
            .imageUrl(announcement.getImageUrl())
            .createdAt(announcement.getCreatedAt())
            .updatedAt(announcement.getUpdatedAt())
            .build())
        .collect(Collectors.toList());

    return AnnouncementListResponse.builder()
        .data(items)
        .build();
  }

  /**
   * 공지사항 상세 조회
   */
  public AnnouncementDetailResponse getAnnouncementDetail(String announcementId) {
    Announcement announcement = announcementRepository.findById(announcementId)
        .orElseThrow(
            () -> new AnnouncementNotFoundException("공지사항을 찾을 수 없습니다. ID: " + announcementId));

    return AnnouncementDetailResponse.builder()
        .announcementId(announcement.getAnnouncementId())
        .title(announcement.getTitle())
        .content(announcement.getContent())
        .imageUrl(announcement.getImageUrl())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();
  }

  /**
   * 공지사항 삭제
   */
  @Transactional
  public AnnouncementDeleteResponse deleteAnnouncement(String announcementId) {
    Announcement announcement = announcementRepository.findById(announcementId)
        .orElseThrow(
            () -> new AnnouncementNotFoundException("공지사항을 찾을 수 없습니다. ID: " + announcementId));

    announcementRepository.delete(announcement);

    return AnnouncementDeleteResponse.builder()
        .message("공지사항이 성공적으로 삭제되었습니다.")
        .announcementId(announcementId)
        .build();
  }
} 