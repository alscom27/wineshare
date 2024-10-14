package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import com.keduit.wineshare.entity.WineReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineReviewRepository extends JpaRepository<WineReview, Long> {

  List<WineReview> findByWine(Wine wine);

  Page<WineReview> findByWineId(Long wineId, Pageable pageable);

}
