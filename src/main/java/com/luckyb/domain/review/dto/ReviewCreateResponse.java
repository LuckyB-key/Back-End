package com.luckyb.domain.review.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewCreateResponse {

    private boolean success;
    private ReviewData data;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReviewData {
        private String id;
        private String shelterId;
        private  String userNickname;
        private String userId;
        private String text;
        private int rating;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
}
