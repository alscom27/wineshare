package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.WineDevelopDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WineDevelopRepositoryCustom {

  Page<WineDevelopDTO> getDevelopPageByWine(Long wineId, Pageable pageable);
}
