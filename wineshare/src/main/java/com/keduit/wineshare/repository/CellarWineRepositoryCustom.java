package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.CellarDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CellarWineRepositoryCustom {

  Page<CellarDetailDTO> getCellarWines(Pageable pageable, Long cellarId);
}
