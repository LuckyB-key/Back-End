package com.luckyb.domain.shelter.service;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Slf4j
@Service
public class CongestionPredictionService {

    private final Random random = new Random();

    /**
     * 현재 수용 인원 계산 (기본 구현)
     * 추후 실시간 QR 체크인 데이터와 연동
     */
    public int calculateCurrentOccupancy(Shelter shelter) {
        // 현재는 시뮬레이션 데이터로 구현
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int baseOccupancy = calculateBaseOccupancy(shelter, hour);
        
        // 랜덤 요소 추가 (±20%)
        int randomVariation = (int) (baseOccupancy * 0.2 * (random.nextDouble() - 0.5) * 2);
        int currentOccupancy = baseOccupancy + randomVariation;
        
        // 최소 0, 최대 수용인원을 넘지 않도록 제한
        return Math.max(0, Math.min(currentOccupancy, shelter.getCapacity()));
    }

    /**
     * 특정 시간의 혼잡도 예측
     */
    public int predictOccupancy(Shelter shelter, LocalDate date, LocalTime time) {
        if (date == null) {
            date = LocalDate.now();
        }
        if (time == null) {
            time = LocalTime.now();
        }
        
        int hour = time.getHour();
        int baseOccupancy = calculateBaseOccupancy(shelter, hour);
        
        // 요일별 가중치 적용
        double dayOfWeekWeight = getDayOfWeekWeight(date.getDayOfWeek());
        
        // 계절별 가중치 적용
        double seasonalWeight = getSeasonalWeight(date);
        
        // 날씨 가중치 (현재는 기본값, 추후 날씨 API 연동)
        double weatherWeight = getWeatherWeight(date);
        
        // 최종 예측값 계산
        double predictedOccupancy = baseOccupancy * dayOfWeekWeight * seasonalWeight * weatherWeight;
        
        // 수용인원을 넘지 않도록 제한
        return Math.max(0, Math.min((int) Math.round(predictedOccupancy), shelter.getCapacity()));
    }

    /**
     * 혼잡도 상태 계산
     */
    public String calculateCongestionStatus(int occupancy, int capacity) {
        if (capacity <= 0) return "알 수 없음";
        
        double ratio = (double) occupancy / capacity;
        
        if (ratio >= 0.9) return "매우 혼잡";
        if (ratio >= 0.7) return "혼잡";
        if (ratio >= 0.4) return "보통";
        if (ratio >= 0.2) return "여유로움";
        return "한산함";
    }

    /**
     * 시간대별 기본 수용 인원 계산
     */
    private int calculateBaseOccupancy(Shelter shelter, int hour) {
        int capacity = shelter.getCapacity();
        double occupancyRate;
        
        // 시간대별 기본 점유율
        if (hour >= 6 && hour < 9) {        // 아침 (6-9시)
            occupancyRate = 0.3;
        } else if (hour >= 9 && hour < 11) { // 오전 (9-11시)
            occupancyRate = 0.4;
        } else if (hour >= 11 && hour < 14) { // 점심 (11-14시) - 피크
            occupancyRate = 0.8;
        } else if (hour >= 14 && hour < 17) { // 오후 (14-17시)
            occupancyRate = 0.6;
        } else if (hour >= 17 && hour < 20) { // 저녁 (17-20시) - 피크
            occupancyRate = 0.7;
        } else if (hour >= 20 && hour < 22) { // 밤 (20-22시)
            occupancyRate = 0.4;
        } else {                             // 새벽/심야 (22-6시)
            occupancyRate = 0.1;
        }
        
        // 쉼터 타입별 조정
        occupancyRate *= getShelterTypeMultiplier(shelter.getType());
        
        return (int) Math.round(capacity * occupancyRate);
    }

    /**
     * 쉼터 타입별 가중치
     */
    private double getShelterTypeMultiplier(Shelter.ShelterType type) {
        return switch (type) {
            case PUBLIC -> 1.2;        // 공공시설 - 이용률 높음
            case COMMUNITY -> 1.0;     // 커뮤니티 시설 - 기본
            case COMMERCIAL -> 0.8;    // 상업시설 - 이용률 낮음 (영업시간 제한)
            case PRIVATE -> 0.9;       // 민간시설 - 약간 낮음
        };
    }

    /**
     * 요일별 가중치
     */
    private double getDayOfWeekWeight(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY -> 1.0;    // 평일 기본
            case FRIDAY -> 1.1;                                 // 금요일 약간 높음
            case SATURDAY -> 1.3;                               // 토요일 높음
            case SUNDAY -> 1.2;                                 // 일요일 높음
        };
    }

    /**
     * 계절별 가중치
     */
    private double getSeasonalWeight(LocalDate date) {
        int month = date.getMonthValue();
        
        // 여름철 (6-8월) 폭염 시즌
        if (month >= 6 && month <= 8) {
            return 1.5; // 여름철 쉼터 이용률 증가
        }
        // 겨울철 (12-2월) 한파 시즌
        else if (month == 12 || month <= 2) {
            return 1.2; // 겨울철 약간 증가
        }
        // 봄, 가을
        else {
            return 0.8; // 쾌적한 날씨로 이용률 감소
        }
    }

    /**
     * 날씨 가중치 (추후 날씨 API 연동)
     */
    private double getWeatherWeight(LocalDate date) {
        // TODO: 실제 날씨 데이터 연동
        // 현재는 기본값 반환
        // 폭염주의보: 1.8, 폭염경보: 2.0
        // 한파주의보: 1.3, 한파경보: 1.5
        // 비: 0.7, 눈: 0.8
        return 1.0;
    }

    /**
     * 혼잡도 예측 신뢰도 계산
     */
    public double calculatePredictionConfidence(Shelter shelter, LocalDate date, LocalTime time) {
        // 기본 신뢰도: 70%
        double baseConfidence = 0.7;
        
        // 시간이 현재에 가까울수록 신뢰도 증가
        LocalDate today = LocalDate.now();
        long daysDiff = Math.abs(date.toEpochDay() - today.toEpochDay());
        
        if (daysDiff == 0) {
            baseConfidence += 0.2; // 당일 예측: +20%
        } else if (daysDiff <= 1) {
            baseConfidence += 0.1; // 1일 이내: +10%
        } else if (daysDiff > 7) {
            baseConfidence -= 0.2; // 1주일 후: -20%
        }
        
        // 데이터 충분성에 따른 조정 (현재는 기본값)
        // TODO: 실제 데이터 축적량에 따라 조정
        
        return Math.max(0.3, Math.min(0.95, baseConfidence));
    }

    /**
     * 시간대별 혼잡도 예측 (하루 전체)
     */
    public int[] predictDailyOccupancy(Shelter shelter, LocalDate date) {
        int[] hourlyOccupancy = new int[24];
        
        for (int hour = 0; hour < 24; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            hourlyOccupancy[hour] = predictOccupancy(shelter, date, time);
        }
        
        return hourlyOccupancy;
    }

    /**
     * 추천 방문 시간 계산
     */
    public LocalTime[] getRecommendedVisitTimes(Shelter shelter, LocalDate date) {
        int[] hourlyOccupancy = predictDailyOccupancy(shelter, date);
        
        // 가장 한산한 시간 3개 찾기
        return java.util.stream.IntStream.range(8, 20) // 운영시간 (8-20시) 내에서만
                .boxed()
                .sorted((h1, h2) -> Integer.compare(hourlyOccupancy[h1], hourlyOccupancy[h2]))
                .limit(3)
                .map(hour -> LocalTime.of(hour, 0))
                .toArray(LocalTime[]::new);
    }
} 