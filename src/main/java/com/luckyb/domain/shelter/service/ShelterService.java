package com.luckyb.domain.shelter.service;

import com.luckyb.domain.shelter.dto.*;
import com.luckyb.domain.shelter.entity.Address;
import com.luckyb.domain.shelter.entity.Coordinates;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.shelter.repository.ShelterRepository;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.ShelterNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final LocationService locationService;
    private final RecommendationService recommendationService;
    private final CongestionPredictionService congestionPredictionService;
    
    // 기본 검색 반경 (km)
    private static final double DEFAULT_SEARCH_RADIUS = 5.0;

    /**
     * 쉼터 목록 조회 (위치 기반)
     */
    public List<ShelterListResponse> getShelterList(ShelterListRequest request) {
        validateCoordinates(request.getLat(), request.getLng());
        
        double searchRadius = request.getDistance() != null ? request.getDistance() : DEFAULT_SEARCH_RADIUS;
        List<String> facilitiesList = parseFacilities(request.getFacilities());
        
        // 모든 활성 쉼터 조회
        List<Shelter> allShelters = shelterRepository.findAllActiveShelters();
        
        // 거리 계산 및 필터링
        return allShelters.stream()
                .map(shelter -> {
                    double distance = locationService.calculateDistance(
                        request.getLat(), request.getLng(),
                        shelter.getCoordinates().getLat(), shelter.getCoordinates().getLng()
                    );
                    return new ShelterWithDistance(shelter, distance);
                })
                .filter(item -> item.distance <= searchRadius)
                .filter(item -> request.getType() == null || 
                        item.shelter.getType().getValue().equals(request.getType()))
                .filter(item -> facilitiesList == null || 
                        facilitiesList.stream().anyMatch(facility -> 
                            item.shelter.getFacilities().contains(facility)))
                .sorted((a, b) -> Double.compare(a.distance, b.distance))
                .map(item -> {
                    String predictedCongestion = calculateBasicCongestion(item.shelter);
                    return ShelterListResponse.from(item.shelter, item.distance, predictedCongestion);
                })
                .collect(Collectors.toList());
    }

    /**
     * 쉼터 상세 조회
     */
    public ShelterDetailResponse getShelterDetail(String shelterId) {
        Shelter shelter = findActiveShelterById(shelterId);
        return ShelterDetailResponse.from(shelter);
    }

    /**
     * 쉼터 등록
     */
    @Transactional
    public ShelterCreateResponse createShelter(ShelterCreateRequest request) {
        try {
            validateShelterCreateRequest(request);
            
            Shelter shelter = request.toEntity();
            Shelter savedShelter = shelterRepository.save(shelter);
            
            log.info("새 쉼터가 등록되었습니다. shelterId: {}, name: {}", 
                    savedShelter.getShelterId(), savedShelter.getName());
            
            return ShelterCreateResponse.from(savedShelter);
            
        } catch (Exception e) {
            log.error("쉼터 등록 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException(ErrorCode.SHELTER_CREATION_FAILED.getMessage(), e);
        }
    }

    /**
     * 쉼터 수정
     */
    @Transactional
    public ShelterUpdateResponse updateShelter(String shelterId, ShelterUpdateRequest request) {
        try {
            Shelter shelter = findActiveShelterById(shelterId);
            validateShelterUpdateRequest(request);
            
            shelter.updateShelterInfo(
                request.getName(),
                request.getAddress() != null ? request.getAddress().toEntity() : null,
                request.getCoordinates() != null ? request.getCoordinates().toEntity() : null,
                request.getShelterType(),
                request.getCapacity(),
                request.getFacilities(),
                request.getOperatingHours(),
                request.getDescription()
            );
            
            Shelter updatedShelter = shelterRepository.save(shelter);
            
            log.info("쉼터 정보가 수정되었습니다. shelterId: {}", shelterId);
            
            return ShelterUpdateResponse.from(updatedShelter);
            
        } catch (ShelterNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("쉼터 수정 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException(ErrorCode.SHELTER_UPDATE_FAILED.getMessage(), e);
        }
    }

    /**
     * 쉼터 삭제 (소프트 삭제)
     */
    @Transactional
    public ShelterDeleteResponse deleteShelter(String shelterId) {
        try {
            Shelter shelter = findActiveShelterById(shelterId);
            
            // 소프트 삭제: 상태를 INACTIVE로 변경
            shelter.updateStatus(Shelter.ShelterStatus.INACTIVE);
            shelterRepository.save(shelter);
            
            log.info("쉼터가 삭제되었습니다. shelterId: {}", shelterId);
            
            return ShelterDeleteResponse.of(shelterId, "쉼터가 성공적으로 삭제되었습니다.");
            
        } catch (ShelterNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("쉼터 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException(ErrorCode.SHELTER_DELETE_FAILED.getMessage(), e);
        }
    }

    /**
     * AI 쉼터 추천
     */
    public List<ShelterRecommendationResponse> getRecommendedShelters(ShelterRecommendationRequest request) {
        validateCoordinates(request.getLat(), request.getLng());
        
        double searchRadius = request.getDistance() != null ? request.getDistance() : DEFAULT_SEARCH_RADIUS;
        List<String> preferencesList = recommendationService.parseUserPreferences(request.getPreferences());
        
        // 반경 내 모든 쉼터 조회
        List<Shelter> allShelters = shelterRepository.findAllActiveShelters();
        
        // AI 추천 알고리즘을 적용하여 정렬
        return allShelters.stream()
                .map(shelter -> {
                    double distance = locationService.calculateDistance(
                        request.getLat(), request.getLng(),
                        shelter.getCoordinates().getLat(), shelter.getCoordinates().getLng()
                    );
                    return new ShelterWithDistance(shelter, distance);
                })
                .filter(item -> item.distance <= searchRadius)
                .map(item -> {
                    // 추천 점수 계산
                    double score = recommendationService.calculateRecommendationScore(
                        item.shelter, request.getLat(), request.getLng(), preferencesList);
                    
                    // 시간대별 가중치 적용
                    score *= recommendationService.getTimeBasedWeight();
                    
                    String predictedCongestion = congestionPredictionService.calculateCongestionStatus(
                        congestionPredictionService.calculateCurrentOccupancy(item.shelter),
                        item.shelter.getCapacity()
                    );
                    
                    return new ShelterWithScore(
                        ShelterRecommendationResponse.from(item.shelter, item.distance, predictedCongestion),
                        score
                    );
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore())) // 점수 높은 순 정렬
                .limit(10) // 상위 10개만 반환
                .map(ShelterWithScore::getResponse)
                .collect(Collectors.toList());
    }

    /**
     * 혼잡도 예측
     */
    public CongestionResponse getCongestionPrediction(String shelterId, CongestionRequest request) {
        Shelter shelter = findActiveShelterById(shelterId);
        
        // 현재 수용 인원 계산
        int currentOccupancy = congestionPredictionService.calculateCurrentOccupancy(shelter);
        
        // 예측 수용 인원 계산
        LocalDate date = request.getDate() != null ? LocalDate.parse(request.getDate()) : LocalDate.now();
        LocalTime time = request.getTime() != null ? LocalTime.parse(request.getTime()) : LocalTime.now();
        int predictedOccupancy = congestionPredictionService.predictOccupancy(shelter, date, time);
        
        // 혼잡도 상태 계산
        String congestionStatus = congestionPredictionService.calculateCongestionStatus(
            predictedOccupancy, shelter.getCapacity());
        
        return CongestionResponse.of(
            congestionStatus,
            currentOccupancy,
            predictedOccupancy,
            shelter.getCapacity()
        );
    }

    // === Private Helper Methods ===

    private Shelter findActiveShelterById(String shelterId) {
        return shelterRepository.findActiveShelterById(shelterId)
                .orElseThrow(() -> new ShelterNotFoundException(ErrorCode.SHELTER_NOT_FOUND, shelterId));
    }

    private void validateCoordinates(Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new IllegalArgumentException(ErrorCode.INVALID_COORDINATES.getMessage());
        }
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new IllegalArgumentException(ErrorCode.INVALID_COORDINATES.getMessage());
        }
    }

    private void validateShelterCreateRequest(ShelterCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("쉼터 이름은 필수입니다.");
        }
        if (request.getCoordinates() == null) {
            throw new IllegalArgumentException("쉼터 좌표는 필수입니다.");
        }
        validateCoordinates(request.getCoordinates().getLat(), request.getCoordinates().getLng());
    }

    private void validateShelterUpdateRequest(ShelterUpdateRequest request) {
        if (request.getCoordinates() != null) {
            validateCoordinates(request.getCoordinates().getLat(), request.getCoordinates().getLng());
        }
    }

    private List<String> parseFacilities(String facilitiesString) {
        if (facilitiesString == null || facilitiesString.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(facilitiesString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }



    private String calculateBasicCongestion(Shelter shelter) {
        // 기본 혼잡도 계산 로직 (목록 조회용)
        return congestionPredictionService.calculateCongestionStatus(
            congestionPredictionService.calculateCurrentOccupancy(shelter),
            shelter.getCapacity()
        );
    }

    // 추천 점수와 응답을 함께 저장하는 내부 클래스
    private static class ShelterWithScore {
        private final ShelterRecommendationResponse response;
        private final double score;

        public ShelterWithScore(ShelterRecommendationResponse response, double score) {
            this.response = response;
            this.score = score;
        }

        public ShelterRecommendationResponse getResponse() {
            return response;
        }

        public double getScore() {
            return score;
        }
    }

    // 쉼터와 거리를 함께 저장하는 내부 클래스
    private static class ShelterWithDistance {
        private final Shelter shelter;
        private final double distance;

        public ShelterWithDistance(Shelter shelter, double distance) {
            this.shelter = shelter;
            this.distance = distance;
        }
    }
} 