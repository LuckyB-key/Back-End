package com.luckyb.domain.announcement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementUpdateRequest {
    private String title;
    private String content;
} 