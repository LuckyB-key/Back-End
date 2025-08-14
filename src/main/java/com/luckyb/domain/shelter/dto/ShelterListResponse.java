package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShelterListResponse {
    
    private String id;
    private String name;
    private String address;
    private Double distance;              // 백엔드가 카카오맵 API로 계산한 거리
    private String status;
    private String predictedCongestion;
    private CoordinatesDto coordinates;   // 지도에 쉼터 마커를 표시할 좌표
    
    public static ShelterListResponse from(Shelter shelter, Double distance, String predictedCongestion) {
        return new ShelterListResponse(
            shelter.getShelterId(),
            shelter.getName(),
            buildAddressString(shelter),
            distance,
            shelter.getStatus().getValue(),
            predictedCongestion,
            CoordinatesDto.from(shelter.getCoordinates())
        );
    }
    
    private static String buildAddressString(Shelter shelter) {
        if (shelter.getAddress() == null) {
            return "";
        }
        // 도로명 주소가 있으면 도로명 주소, 없으면 지번 주소 사용
        String roadAddress = shelter.getAddress().getRoad();
        String jibunAddress = shelter.getAddress().getJibun();
        
        if (roadAddress != null && !roadAddress.trim().isEmpty()) {
            return roadAddress;
        } else if (jibunAddress != null && !jibunAddress.trim().isEmpty()) {
            return jibunAddress;
        } else {
            return "";
        }
    }
} 