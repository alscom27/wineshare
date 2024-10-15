package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.WineReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WineReviewRepositoryCustom {

  Page<WineReviewDTO> getReviewPageByWine(Long wineId, Pageable pageable);
}
