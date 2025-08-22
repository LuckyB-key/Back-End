package com.luckyb.domain.checkin.controller;

import com.luckyb.domain.checkin.dto.CheckinResponse;
import com.luckyb.domain.checkin.dto.MyCheckinListRequest;
import com.luckyb.domain.checkin.dto.MyCheckinListResponse;
import com.luckyb.domain.checkin.service.CheckinService;
import com.luckyb.domain.user.entity.User;
import com.luckyb.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @PostMapping("/shelters/{shelterId}/checkins")
    public ApiResponse<CheckinResponse> checkin(
            @AuthenticationPrincipal User user,
            @PathVariable String shelterId) {
        CheckinResponse response = checkinService.checkin(user, shelterId);
        return ApiResponse.success(response);
    }

    @GetMapping("/users/me/checkins")
    public ApiResponse<MyCheckinListResponse> getMyCheckins(
            @AuthenticationPrincipal User user,
            @ModelAttribute MyCheckinListRequest request) {
        MyCheckinListResponse response = checkinService.getMyCheckins(user, request);
        return ApiResponse.success(response);
    }
} 