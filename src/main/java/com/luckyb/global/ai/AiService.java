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
    List<Map<String, Object>> recommendShelters(double lat, double lng, List<String> preferences, String category);
    
    /**
     * 혼잡도 예측을 위한 AI 분석
     */
    Map<String, Object> predictCongestion(String shelterId, String date, String time);
    

} 