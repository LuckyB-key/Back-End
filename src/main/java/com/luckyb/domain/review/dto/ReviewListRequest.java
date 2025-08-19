package com.luckyb.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListRequest {
    private Integer page=0;
    private Integer size=10;

}
