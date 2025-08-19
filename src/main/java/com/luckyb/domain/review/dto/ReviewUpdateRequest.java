package com.luckyb.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewUpdateRequest {
    private String text;
    private int rating;
    private List<String> photoUrls;

}
