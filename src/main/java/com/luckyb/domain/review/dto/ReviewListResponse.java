package com.luckyb.domain.review.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReviewListResponse {
    private String id;
    private String userId;
    private String userNickname;
    private String text;
    private int rating;
    private List<String> photoUrls;
    private String createdAt;



}
