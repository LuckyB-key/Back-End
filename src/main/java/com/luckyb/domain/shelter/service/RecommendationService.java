package com.luckyb.domain.shelter.service;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final LocationService locationService;

    /**
     * 쉼터 추천 점수 계산
     * 거리, 편의시설, 용량, 현재 혼잡도 등을 종합적으로 고려
     */
    public double calculateRecommendationScore(Shelter shelter, double userLat, double userLng, 
                                             List<String> userPreferences) {
        double score = 0.0;
        
        // 1. 거리 점수 (가까울수록 높은 점수) - 40% 가중치
        double distance = locationService.calculateDistance(userLat, userLng, 
                                                           shelter.getCoordinates().getLat(), 
                                                           shelter.getCoordinates().getLng());
        double distanceScore = calculateDistanceScore(distance);
        score += distanceScore * 0.4;
        
        // 2. 편의시설 매칭 점수 - 30% 가중치
        double facilityScore = calculateFacilityScore(shelter.getFacilities(), userPreferences);
        score += facilityScore * 0.3;
        
        // 3. 용량 점수 (큰 쉼터일수록 높은 점수) - 20% 가중치
        double capacityScore = calculateCapacityScore(shelter.getCapacity());
        score += capacityScore * 0.2;
        
        // 4. 혼잡도 점수 (덜 혼잡할수록 높은 점수) - 10% 가중치
        double congestionScore = calculateCongestionScore(shelter);
        score += congestionScore * 0.1;
        
        log.debug("Shelter {} score: distance={}, facility={}, capacity={}, congestion={}, total={}", 
                 shelter.getName(), distanceScore, facilityScore, capacityScore, congestionScore, score);
        
        return score;
    }

    /**
     * 거리 기반 점수 계산 (0-100점)
     * 0-1km: 100점, 1-3km: 80점, 3-5km: 60점, 5km+: 40점
     */
    private double calculateDistanceScore(double distanceKm) {
        if (distanceKm <= 1.0) return 100.0;
        if (distanceKm <= 3.0) return 80.0;
        if (distanceKm <= 5.0) return 60.0;
        return Math.max(40.0 - (distanceKm - 5.0) * 5, 0);
    }

    /**
     * 편의시설 매칭 점수 계산 (0-100점)
     */
    private double calculateFacilityScore(List<String> shelterFacilities, List<String> userPreferences) {
        if (userPreferences == null || userPreferences.isEmpty()) {
            return 50.0; // 선호도가 없으면 중간 점수
        }
        
        if (shelterFacilities == null || shelterFacilities.isEmpty()) {
            return 30.0; // 시설 정보가 없으면 낮은 점수
        }
        
        // 사용자 선호 시설과 쉼터 시설의 매칭률 계산
        long matchCount = userPreferences.stream()
                .mapToLong(pref -> shelterFacilities.stream()
                        .anyMatch(facility -> facility.toLowerCase().contains(pref.toLowerCase())) ? 1 : 0)
                .sum();
        
        double matchRate = (double) matchCount / userPreferences.size();
        return matchRate * 100.0;
    }

    /**
     * 용량 기반 점수 계산 (0-100점)
     */
    private double calculateCapacityScore(Integer capacity) {
        if (capacity == null || capacity <= 0) return 30.0;
        
        // 10명 이하: 50점, 20명 이하: 70점, 50명 이하: 90점, 50명 초과: 100점
        if (capacity <= 10) return 50.0;
        if (capacity <= 20) return 70.0;
        if (capacity <= 50) return 90.0;
        return 100.0;
    }

    /**
     * 혼잡도 기반 점수 계산 (0-100점)
     * 현재는 기본 로직, 추후 실시간 데이터 연동
     */
    private double calculateCongestionScore(Shelter shelter) {
        // 현재 시간을 기준으로 혼잡도 추정
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        // 피크 시간대 (11-14시, 17-19시)는 혼잡도가 높다고 가정
        if ((hour >= 11 && hour <= 14) || (hour >= 17 && hour <= 19)) {
            return 60.0; // 피크 시간대는 낮은 점수
        } else if (hour >= 8 && hour <= 21) {
            return 80.0; // 일반 시간대
        } else {
            return 90.0; // 새벽/늦은 밤은 한산하다고 가정
        }
    }

    /**
     * 시설 타입별 우선순위 점수
     */
    public double getTypePreferenceScore(Shelter.ShelterType type) {
        return switch (type) {
            case PUBLIC -> 90.0;        // 공공시설 우선
            case COMMUNITY -> 80.0;     // 커뮤니티 시설
            case PRIVATE -> 70.0;       // 민간시설
            case COMMERCIAL -> 60.0;    // 상업시설
        };
    }

    /**
     * 사용자 선호도 문자열을 리스트로 파싱
     */
    public List<String> parseUserPreferences(String preferencesString) {
        if (preferencesString == null || preferencesString.trim().isEmpty()) {
            return List.of();
        }
        
        return Arrays.stream(preferencesString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 시간대별 추천 가중치 계산
     */
    public double getTimeBasedWeight() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        // 폭염 시간대 (10-18시)에는 높은 가중치
        if (hour >= 10 && hour <= 18) {
            return 1.2;
        } else if (hour >= 8 && hour <= 21) {
            return 1.0;
        } else {
            return 0.8; // 새벽/늦은 밤은 낮은 가중치
        }
    }

    /**
     * 계절별 추천 가중치 (추후 구현)
     */
    public double getSeasonalWeight() {
        // TODO: 계절별 가중치 구현
        // 여름(6-8월): 냉방시설 가중치 증가
        // 겨울(12-2월): 난방시설 가중치 증가
        return 1.0;
    }
} 