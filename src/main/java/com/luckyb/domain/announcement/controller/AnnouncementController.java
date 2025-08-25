package com.luckyb.domain.announcement.controller;

import com.luckyb.domain.announcement.dto.*;
import com.luckyb.domain.announcement.service.AnnouncementService;
import com.luckyb.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

  private final AnnouncementService announcementService;

  /**
   * 공지사항 등록
   */
  @PostMapping
  public ApiResponse<AnnouncementCreateResponse> createAnnouncement(
      @RequestBody AnnouncementCreateRequest request
  ) {
    AnnouncementCreateResponse response = announcementService.createAnnouncement(request);
    log.info("새 공지사항이 등록되었습니다. announcementId: {}", response.getAnnouncementId());
    return ApiResponse.success(response);
  }

  /**
   * 공지사항 수정
   */
  @PutMapping("/{announcementId}")
  public ApiResponse<AnnouncementUpdateResponse> updateAnnouncement(
      @PathVariable String announcementId,
      @RequestBody AnnouncementUpdateRequest request
  ) {
    AnnouncementUpdateResponse response = announcementService.updateAnnouncement(announcementId,
        request);
    log.info("공지사항이 수정되었습니다. announcementId: {}", announcementId);
    return ApiResponse.success(response);
  }

  /**
   * 공지사항 목록 조회
   */
  @GetMapping
  public ApiResponse<AnnouncementListResponse> getAnnouncementList(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size
  ) {
    AnnouncementListRequest request = new AnnouncementListRequest();
    request.setPage(page);
    request.setSize(size);

    AnnouncementListResponse response = announcementService.getAnnouncementList(request);
    return ApiResponse.success(response);
  }

  /**
   * 공지사항 상세 조회
   */
  @GetMapping("/{announcementId}")
  public ApiResponse<AnnouncementDetailResponse> getAnnouncementDetail(
      @PathVariable String announcementId
  ) {
    AnnouncementDetailResponse response = announcementService.getAnnouncementDetail(announcementId);
    return ApiResponse.success(response);
  }

  /**
   * 공지사항 삭제
   */
  @DeleteMapping("/{announcementId}")
  public ApiResponse<AnnouncementDeleteResponse> deleteAnnouncement(
      @PathVariable String announcementId
  ) {
    AnnouncementDeleteResponse response = announcementService.deleteAnnouncement(announcementId);
    log.info("공지사항이 삭제되었습니다. announcementId: {}", announcementId);
    return ApiResponse.success(response);
  }
} 