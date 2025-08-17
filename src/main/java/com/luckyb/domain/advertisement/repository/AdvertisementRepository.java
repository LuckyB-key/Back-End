package com.luckyb.domain.advertisement.repository;

import com.luckyb.domain.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

}
