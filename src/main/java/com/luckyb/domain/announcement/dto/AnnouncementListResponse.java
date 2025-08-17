package com.luckyb.domain.announcement.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AnnouncementListResponse {
    private List<AnnouncementItem> data;

    @Getter
    @Builder
    public static class AnnouncementItem {
        private String announcementId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
    }
} 