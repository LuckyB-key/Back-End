package com.luckyb.domain.like.repository;

import com.luckyb.domain.like.entity.Like;
import com.luckyb.domain.shelter.entity.Shelter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  List<Like> findAllByShelter(Shelter shelter);
}