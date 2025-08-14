package com.luckyb.domain.shelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationService {

    /**
     * 하버사인 공식을 사용하여 두 지점 간의 거리를 계산 (km 단위)
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // 지구의 반지름 (km)
        final double EARTH_RADIUS = 6371.0;
        
        // 위도와 경도를 라디안으로 변환
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }

    /**
     * 좌표 유효성 검증
     */
    public boolean isValidCoordinates(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }

    /**
     * 한국 영토 내 좌표인지 검증 (대략적인 범위)
     */
    public boolean isWithinKorea(double lat, double lng) {
        // 한국의 대략적인 좌표 범위
        return lat >= 33.0 && lat <= 38.7 && lng >= 124.5 && lng <= 131.9;
    }

    /**
     * 거리 기반으로 두 지점이 지정된 반경 내에 있는지 확인
     */
    public boolean isWithinRadius(double lat1, double lng1, double lat2, double lng2, double radiusKm) {
        double distance = calculateDistance(lat1, lng1, lat2, lng2);
        return distance <= radiusKm;
    }

    /**
     * 거리를 사용자 친화적인 문자열로 변환
     */
    public String formatDistance(double distanceKm) {
        if (distanceKm < 1.0) {
            return String.format("%.0fm", distanceKm * 1000);
        } else {
            return String.format("%.1fkm", distanceKm);
        }
    }

    /**
     * 카카오맵 API를 사용한 도보 거리 계산 (추후 구현 예정)
     * 현재는 직선 거리로 대체
     */
    public double calculateWalkingDistance(double lat1, double lng1, double lat2, double lng2) {
        // TODO: 카카오맵 API 연동하여 실제 도보 거리 계산
        // 현재는 직선 거리에 1.3 배수를 적용하여 대략적인 도보 거리 추정
        double straightDistance = calculateDistance(lat1, lng1, lat2, lng2);
        return straightDistance * 1.3;
    }

    /**
     * 카카오맵 API를 사용한 도보 시간 계산 (추후 구현 예정)
     * 현재는 평균 도보 속도로 계산
     */
    public int calculateWalkingTimeMinutes(double lat1, double lng1, double lat2, double lng2) {
        // TODO: 카카오맵 API 연동하여 실제 도보 시간 계산
        // 현재는 평균 도보 속도 4km/h로 계산
        double walkingDistance = calculateWalkingDistance(lat1, lng1, lat2, lng2);
        return (int) Math.ceil(walkingDistance / 4.0 * 60); // 분 단위
    }
} 