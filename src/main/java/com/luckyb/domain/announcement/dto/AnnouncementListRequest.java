package com.luckyb.domain.announcement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementListRequest {

  private Integer page = 0;
  private Integer size = 10;
}