package com.luckyb.domain.shelter.repository;

import com.luckyb.domain.shelter.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {
    
    /**
     * 쉼터 ID로 활성 상태인 쉼터 조회
     */
    @Query("SELECT s FROM Shelter s WHERE s.shelterId = :shelterId AND s.status != 'INACTIVE'")
    Optional<Shelter> findActiveShelterById(@Param("shelterId") String shelterId);
    
    /**
     * 쉼터 타입으로 조회
     */
    List<Shelter> findByTypeAndStatus(Shelter.ShelterType type, Shelter.ShelterStatus status);
    
    /**
     * 특정 편의시설을 가진 쉼터 조회
     */
    @Query("SELECT s FROM Shelter s JOIN s.facilities f WHERE f IN :facilities AND s.status = 'ACTIVE'")
    List<Shelter> findByFacilitiesContaining(@Param("facilities") List<String> facilities);
    
    /**
     * 위치 기반으로 반경 내 쉼터 조회 (간단한 구현)
     * 실제 프로덕션에서는 PostGIS 등을 사용하는 것이 좋음
     */
    @Query("SELECT s FROM Shelter s WHERE s.status = 'ACTIVE'")
    List<Shelter> findAllActiveShelters();
    
    /**
     * 위치, 타입, 편의시설 조건으로 쉼터 검색
     */
    @Query(value = """
        SELECT DISTINCT s.*, 
               (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * 
                           cos(radians(s.longitude) - radians(:lng)) + 
                           sin(radians(:lat)) * sin(radians(s.latitude)))) AS distance
        FROM shelters s 
        LEFT JOIN shelter_facilities sf ON s.shelter_id = sf.shelter_id
        WHERE s.status = 'ACTIVE'
          AND (:type IS NULL OR s.type = :type)
          AND (:facilities IS NULL OR sf.facility IN :facilities)
        HAVING distance <= :radiusKm
        ORDER BY distance
        """, nativeQuery = true)
    List<Object[]> findSheltersWithFilters(@Param("lat") Double lat,
                                          @Param("lng") Double lng,
                                          @Param("radiusKm") Double radiusKm,
                                          @Param("type") String type,
                                          @Param("facilities") List<String> facilities);
    
    /**
     * 활성 상태인 모든 쉼터 조회
     */
    List<Shelter> findByStatus(Shelter.ShelterStatus status);
    
    /**
     * 쉼터 이름으로 검색 (부분 일치)
     */
    List<Shelter> findByNameContainingIgnoreCaseAndStatus(String name, Shelter.ShelterStatus status);
    
    /**
     * 특정 위치에서 가장 가까운 쉼터들 조회 (제한된 개수)
     */
    @Query(value = """
        SELECT s.*, 
               (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * 
                           cos(radians(s.longitude) - radians(:lng)) + 
                           sin(radians(:lat)) * sin(radians(s.latitude)))) AS distance
        FROM shelters s 
        WHERE s.status = 'ACTIVE'
        ORDER BY distance
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findNearestShelters(@Param("lat") Double lat,
                                      @Param("lng") Double lng,
                                      @Param("limit") Integer limit);
} 