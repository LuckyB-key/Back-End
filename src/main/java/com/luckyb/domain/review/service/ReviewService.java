package com.luckyb.domain.review.service;

import com.luckyb.domain.review.dto.*;
import com.luckyb.domain.review.entity.Review;
import com.luckyb.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    @Transactional
    public ReviewCreateResponse createReview(String shelterId, String userId, ReviewCreateRequest request) {

        // Review 엔티티 생성
        Review review = Review.builder()

                .shelterId(shelterId)
                .userId(userId)
                .userNickname(request.getUserNickname())
                .text(request.getText())
                .rating(request.getRating())
                .photoUrls(request.getPhotoUrls())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // DB에 저장
        reviewRepository.save(review);

        // ReviewCreateResponse용 DTO 생성
        ReviewCreateResponse.ReviewData reviewData = ReviewCreateResponse.ReviewData.builder()
                .id(review.getId())
                .shelterId(review.getShelterId())
                .userId(review.getUserId())
                .text(review.getText())
                .rating(review.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 응답 반환
        return new ReviewCreateResponse(true, reviewData);
    }

    // 리뷰 수정
    @Transactional
    public ReviewUpdateResponse updateReview(String reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(String.valueOf(Long.valueOf(reviewId)))
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        review.setText(request.getText());
        review.setRating(request.getRating());
        review.setPhotoUrls(request.getPhotoUrls());

        // ReviewData 생성
        ReviewUpdateResponse.ReviewData reviewData = ReviewUpdateResponse.ReviewData.builder()
                .id(review.getId())
                .text(review.getText())
                .rating(review.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return new ReviewUpdateResponse(true, reviewData);
    }



    // 리뷰 삭제
    @Transactional
    public ReviewDeleteResponse deleteReview(String reviewId) {
        reviewRepository.deleteById(String.valueOf(Long.valueOf(reviewId)));
        return new ReviewDeleteResponse(true, new ReviewDeleteResponse.DeleteData("리뷰가 삭제되었습니다."));
    }

    @Transactional
    public List<ReviewListResponse> getReviews(String shelterId, ReviewListRequest request) {
        List<Review> reviews = reviewRepository.findByShelterId(shelterId);

        List<ReviewListResponse> reviewDataList = reviews.stream()
                .map(r -> new ReviewListResponse(
                        r.getId(),
                        r.getUserId(),
                        r.getUserNickname(),
                        r.getText(),
                        r.getRating(),
                        r.getPhotoUrls(),
                        r.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        return reviewDataList; // Controller에서 List<ReviewListResponse>로 반환
    }


    // 내 리뷰 목록 조회
    @Transactional
    public MyReviewListResponse getMyReviews(String userId, MyReviewListRequest request) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        List<MyReviewListResponse.ReviewData> reviewDataList = reviews.stream()
                .map(r -> {
                    MyReviewListResponse.ReviewData data = new MyReviewListResponse.ReviewData();
                    data.setId(r.getId());
                    data.setUserId(r.getUserId());
                    data.setUserNickname(r.getUserNickname());
                    data.setText(r.getText());
                    data.setRating(r.getRating());
                    data.setPhotoUrls(r.getPhotoUrls());
                    data.setCreatedAt(r.getCreatedAt().toString());
                    return data;
                })
                .collect(Collectors.toList());

        MyReviewListResponse response = new MyReviewListResponse();
        response.setSuccess(true);
        response.setData(reviewDataList);
        return response;
    }
    }


