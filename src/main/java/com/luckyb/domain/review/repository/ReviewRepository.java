package com.luckyb.domain.review.repository;

import com.luckyb.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
List<Review> findByShelterId(String shelterId);
List<Review> findByUserId(String userId);
}
