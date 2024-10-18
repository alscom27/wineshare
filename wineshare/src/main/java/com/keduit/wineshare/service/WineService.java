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
import java.util.*;


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
        wine.getMember().getId(), wine.getWineImg(),
        wine.getMember().getNickname());
    return wineDTO;
  }

  public Wine getWineById(Long id) {
    return wineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public List<Wine> getSimilarWines(Wine wine) {
    List<Wine> similarWines = new ArrayList<>();
    // 1.타입이 같고 전문가 평점 평균이 높은 1개
    Wine sameTypeMostRating = wineRepository.findSameTypeMostRating(wine);
    similarWines.add(sameTypeMostRating);

  // 2.아로마 One이 같고 높은것 1개
    Wine sameMostAroma = wineRepository.findMostFrequentAromaOneWine(wine);
    similarWines.add(sameMostAroma);


   // 3.푸드 One이 같고 전문가 평점 카운트가 높은 1개
    Wine sameMostFood = wineRepository.findMostFrequentFoodOneWine(wine);
    similarWines.add(sameMostFood);


    // 4.국가가 같고 전문가 평점 평균이 높은 1개
    Wine sameCountryMostRating = wineRepository.findSameCountryMostRating(wine);
    similarWines.add(sameCountryMostRating);


    return similarWines;
  }
}

