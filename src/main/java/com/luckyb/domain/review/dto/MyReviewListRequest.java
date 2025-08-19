package com.luckyb.domain.review.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewListRequest {
    private Integer page=0;
    private Integer size=10;
}
