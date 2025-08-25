package com.luckyb.global.ai;

import java.util.List;
import java.util.Map;

public interface AiService {

  /**
   * OpenAI API를 사용하여 텍스트 생성
   */
  String generateText(String prompt);

  /**
   * 쉼터 추천을 위한 AI 분석
   */
  List<Map<String, Object>> recommendShelters(double lat, double lng, String[] preferences,
      String category);

  /**
   * 혼잡도 예측을 위한 AI 분석
   */
  Map<String, Object> predictCongestion(String shelterId, String date, String time);

  /**
   * 맞춤형 광고 추천을 위한 AI 분석
   */
  List<Map<String, Object>> recommendAdvertisements(double lat, double lng, String userId);

  /**
   * 맞춤형 알림 추천을 위한 AI 분석
   */
  List<Map<String, Object>> recommendNotifications(double latitude, double longitude);


} 