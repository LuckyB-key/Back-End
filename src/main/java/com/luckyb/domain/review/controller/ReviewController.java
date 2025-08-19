package com.luckyb.domain.review.controller;


import com.luckyb.domain.review.dto.*;
import com.luckyb.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 쉼터별 리뷰 목록 조회
    @GetMapping("/shelters/{shelterId}/reviews")
    public List<ReviewListResponse> getReviews(
            @PathVariable String shelterId,
            @ModelAttribute ReviewListRequest request) {
        return reviewService.getReviews(shelterId, request);
    }



    // 리뷰 작성
    @PostMapping("/shelters/{shelterId}/reviews")
    public ReviewCreateResponse createReview(
            @PathVariable String shelterId,
            @RequestBody ReviewCreateRequest request) {
        return reviewService.createReview(shelterId, request.getUserId(), request);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ReviewUpdateResponse updateReview(
            @PathVariable String reviewId,
            @RequestBody ReviewUpdateRequest request) {
        return reviewService.updateReview(reviewId, request);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ReviewDeleteResponse deleteReview(@PathVariable String reviewId) {
        return reviewService.deleteReview(reviewId);
    }



    // 특정 유저 리뷰 목록 조회
    @GetMapping("/users/{userId}/reviews")
    public MyReviewListResponse getMyReviews(
            @PathVariable String userId,
            @ModelAttribute MyReviewListRequest request) {
        return reviewService.getMyReviews(userId, request);
    }


//    // 내 리뷰 목록 조회
//    @GetMapping("/users/me/reviews")
//    public MyReviewListResponse getMyReviews(
//            @RequestParam String userId, // 나중에 Spring Security 붙이면 AuthenticationPrincipal로 교체
//            @ModelAttribute MyReviewListRequest request) {
//        return reviewService.getMyReviews(userId, request);
//    }
}
