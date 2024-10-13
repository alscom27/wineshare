package com.keduit.wineshare.service;


import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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


  // 파싱 메소드 만들어보자
  // @PostConstruct 어노테이션이 메소드를 애플리케이션 최초 실행 시 한번 실행 시켜줌
  @PostConstruct
  public void initParsing() {
    String[] wineTypeList = {"reds", "whites", "rose", "port", "dessert", "sparkling"};
    if (wineRepository.count() == 0){ // 데이터 한개라도 있으면 여기서 막힘
      for(String wineType : wineTypeList) {
        try {
          // JSON 을 InputStream 으로 read
          URL url = new URL("https://api.sampleapis.com/wines/" + wineType);
          try(InputStream inputStream = url.openStream()) {
            List<Wine> wines = parseWine(inputStream, wineType);
            wineRepository.saveAll(wines);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }
  // 파싱해서 리스트 반환
  private List<Wine> parseWine(InputStream inputStream, String wineType) throws IOException {
    List<Wine> wines = new ArrayList<>();
    Set<String> wineNames = new HashSet<>(); // 중복 체크용

    // 와인타입 바꿔주기
    WineType wineTypeEnum = null;
    if (wineType.equals("reds")) {
      wineTypeEnum = WineType.RED;
    } else if (wineType.equals("whites")) {
      wineTypeEnum = WineType.WHITE;
    } else if (wineType.equals("rose")) {
      wineTypeEnum = WineType.ROSE;
    } else if (wineType.equals("port")) {
      wineTypeEnum = WineType.PORT;
    } else if (wineType.equals("dessert")) {
      wineTypeEnum = WineType.DESSERT;
    } else if (wineType.equals("sparkling")) {
      wineTypeEnum = WineType.SPARKLING;
    }

    // InputStream을 BufferedReader로 감싸기
    StringBuilder jsonBuilder = new StringBuilder();
    String line;
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      while ((line = bufferedReader.readLine()) != null) {
        jsonBuilder.append(line);
      }
    }

    // JSON 문자열을 JSONArray 로 변환
    JSONArray jsonArray = new JSONArray(jsonBuilder.toString());

    // JSON 객체를 Wine 객체의 형태에 맞춰 담기
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      String wineName = jsonObject.getString("wine");

      if(!wineNames.contains(wineName)){ // 중복체크용 와인이름 리스트에 같은 이름이 없으면 추가
        Wine wine = new Wine();
        wine.setWineName(jsonObject.getString("wine"));
        wine.setWineImg(jsonObject.getString("image"));

        // json 의 로케이션을 분리한 후 각각 나라와 지역으로 담는다
        String[] location = jsonObject.getString("location").split("\\n·\\n");
        if (location.length == 2) {
          wine.setCountry(location[0]);
          wine.setRegion(location[1]);
        } else {
          // 위치 정보가 없거나 다른 경우
          wine.setCountry(location[0]);
          wine.setRegion("Unknown");
        }

        wine.setWineType(wineTypeEnum);
        wines.add(wine);
        wineNames.add(wineName);
      }
    }
    return wines;
  }

  @Transactional(readOnly = true)
  public Page<WineDTO> getWinePage(WineSearchDTO wineSearchDTO, Pageable pageable) {
    return wineRepository.getWinePage(wineSearchDTO, pageable);
  }


}

