package com.keduit.wineshare.service;


import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import java.io.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class WineService {

  private final WineRepository wineRepository;


  // 등록할때 이름 중복체크 후 등록하게끔 처리
  public Wine saveWine(Wine wine) {
    validateWine(wine);
    return wineRepository.save(wine);
  }


  // 와인이름 중복 체크하는 메소드
  private void validateWine(Wine wine) {
    Wine findWine = wineRepository.findByWineName(wine.getWineName());
    if (findWine != null) {
      throw new IllegalStateException("이미 등록된 와인입니다.");
    }
  }




  @Transactional(readOnly = true)
  public Page<WineDTO> getWinePage(WineSearchDTO wineSearchDTO, Pageable pageable) {
    return wineRepository.getWinePage(wineSearchDTO, pageable);
  }


  @Transactional(readOnly = true)
  public WineDTO getWineDetail(Long wineId) {
    Wine wine = wineRepository.findById(wineId).orElseThrow(EntityNotFoundException::new);
    WineDTO wineDTO = new WineDTO(
        wine.getId(), wine.getWineName(),
        wine.getCountry(), wine.getRegion(),
        wine.getPrice(), wine.getWineType(),
        wine.getMember().getId(), wine.getWineImg()); // 각 항목 널일때.. 어떻게 처리할지 다 잡아야하나? 초기값으로 넣은 데이터들은 조인된 멤버테이블의 정보가 없어.
    return wineDTO;
  }

  public Wine getWineById(Long id) {
    return wineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }
}

