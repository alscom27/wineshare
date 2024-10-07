package com.keduit.wineshare.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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


//  public void insert




}
