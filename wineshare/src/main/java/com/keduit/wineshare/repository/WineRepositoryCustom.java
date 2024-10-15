package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.Wine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WineRepositoryCustom {
  Page<WineDTO> getWinePage(WineSearchDTO wineSearchDTO, Pageable pageable);
  Wine findSameTypeMostRating(Wine wine);
  Wine findSameCountryMostRating(Wine wine);
  Wine findMostFrequentAromaOneWine(Wine wine);
  Wine findMostFrequentFoodOneWine(Wine wine);
}

