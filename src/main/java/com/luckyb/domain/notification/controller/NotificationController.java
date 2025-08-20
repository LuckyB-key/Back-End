package com.luckyb.domain.notification.controller;

import com.luckyb.domain.notification.dto.NotificationResponse;
import com.luckyb.domain.notification.service.NotificationService;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.service.UserService;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.InvalidTokenException;
import com.luckyb.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;

  @GetMapping("/notifications")
  public Page<NotificationResponse> getRecentNotifications(
      @RequestHeader("Authorization") String authorization,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    String userId = jwtTokenProvider.getUserIdFromToken(token);

    return notificationService.getRecentNotifications(userId, pageable)
        .map(NotificationResponse::from);
  }

  @PostMapping("/notifications/{notificationId}/read")
  public void readNotification(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("notificationId") String notificationId) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    String userId = jwtTokenProvider.getUserIdFromToken(token);

    notificationService.readNotification(userId, notificationId);
  }

  @PostMapping("/notifications/read-all")
  public void ReadAllNotifications(
      @RequestHeader("Authorization") String authorization) {

    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }

    String userId = jwtTokenProvider.getUserIdFromToken(token);

    notificationService.readAllNotifications(userId);
  }

  @GetMapping("/notifications/settings")
  public boolean getNotificationSetting(@RequestHeader("Authorization") String authorization) {
    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    String userId = jwtTokenProvider.getUserIdFromToken(token);
    return userService.getUserById(userId).isAlarmEnabled();
  }

  @PutMapping("/notifications/settings")
  public void updateNotificationSetting(@RequestHeader("Authorization") String authorization,
      @RequestParam boolean enabled) {
    String token = jwtTokenProvider.resolveToken(authorization);
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    String userId = jwtTokenProvider.getUserIdFromToken(token);
    User user = userService.getUserById(userId);
    user.setAlarmEnabled(enabled);
    userService.saveUser(user);
  }

}
