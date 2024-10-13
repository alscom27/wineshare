package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WineRepositoryCustom {
  Page<WineDTO> getWinePage(WineSearchDTO wineSearchDTO, Pageable pageable);
}

