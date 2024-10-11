package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.WineReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineReviewRepository extends JpaRepository<WineReview, Long> {

  Page<WineReview> findByWineId(Long wineId, Pageable pageable);

}
