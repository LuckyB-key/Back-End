package com.luckyb.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCheckinListResponse {
    private List<CheckinRecord> data;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckinRecord {
        private String checkinId;
        private String shelterId;
        private String shelterName;
        private String checkinTime;
        private String checkoutTime;
    }
} 