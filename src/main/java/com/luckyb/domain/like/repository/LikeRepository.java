package com.luckyb.domain.like.repository;

import com.luckyb.domain.like.entity.Like;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  boolean existsByShelterAndUser(Shelter shelter, User userId);

  void deleteByShelterAndUser(Shelter shelter, User userId);
}