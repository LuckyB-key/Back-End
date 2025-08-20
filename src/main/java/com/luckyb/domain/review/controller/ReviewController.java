package com.luckyb.domain.review.controller;


import com.luckyb.domain.review.dto.*;
import com.luckyb.domain.review.service.ReviewService;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final JwtTokenProvider jwtTokenProvider;

  // 쉼터별 리뷰 목록 조회 (조회는 그대로)
  @GetMapping("/shelters/{shelterId}/reviews")
  public List<ReviewListResponse> getReviews(
      @PathVariable String shelterId,
      @ModelAttribute ReviewListRequest request) {
    return reviewService.getReviews(shelterId, request);
  }

  // 리뷰 작성
  @PostMapping("/shelters/{shelterId}/reviews")
  public ReviewCreateResponse createReview(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String shelterId,
      @RequestBody ReviewCreateRequest request) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    String userId = jwtTokenProvider.getUserIdFromToken(token);

    return reviewService.createReview(shelterId, userId, request);
  }

  // 리뷰 수정
  @PutMapping("/reviews/{reviewId}")
  public ReviewUpdateResponse updateReview(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String reviewId,
      @RequestBody ReviewUpdateRequest request) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    String userId = jwtTokenProvider.getUserIdFromToken(token);

    return reviewService.updateReview(reviewId, userId, request);
  }

  // 리뷰 삭제
  @DeleteMapping("/reviews/{reviewId}")
  public ReviewDeleteResponse deleteReview(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String reviewId) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    String userId = jwtTokenProvider.getUserIdFromToken(token);

    return reviewService.deleteReview(reviewId, userId);
  }

  // 특정 유저 리뷰 목록 조회 (조회는 그대로)
  @GetMapping("/users/{userId}/reviews")
  public MyReviewListResponse getMyReviews(
      @PathVariable String userId,
      @ModelAttribute MyReviewListRequest request) {
    return reviewService.getMyReviews(userId);
  }
}
