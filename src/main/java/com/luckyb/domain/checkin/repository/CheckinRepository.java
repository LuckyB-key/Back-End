package com.luckyb.domain.checkin.repository;

import com.luckyb.domain.checkin.entity.Checkin;
import com.luckyb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

  @Query("SELECT c FROM Checkin c WHERE c.user = :user ORDER BY c.checkinTime DESC")
  Page<Checkin> findByUserOrderByCheckinTimeDesc(@Param("user") User user, Pageable pageable);

  @Query("SELECT c FROM Checkin c WHERE c.user = :user AND c.shelter.shelterId = :shelterId AND c.checkoutTime IS NULL")
  Optional<Checkin> findActiveCheckinByUserAndShelter(@Param("user") User user,
      @Param("shelterId") String shelterId);

  @Query("SELECT COUNT(c) FROM Checkin c WHERE c.shelter.shelterId = :shelterId AND c.checkoutTime IS NULL")
  long countActiveCheckinsByShelter(@Param("shelterId") String shelterId);
} 