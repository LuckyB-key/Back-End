package com.luckyb.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MyReviewListResponse {
    private boolean success;
    private List<ReviewData> data;

    @Getter
    @Setter
    public static class ReviewData{
        private String id;
        private String userId;
        private String userNickname;
        private String text;
        private int rating;
        private List<String> photoUrls;
        private String createdAt;

    }

}
