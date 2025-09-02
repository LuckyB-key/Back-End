package com.luckyb.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewCreateRequest {

  private String userId;
  private String text;
  private String userNickname;
  private int rating;
  private List<String> photoUrls;

}
