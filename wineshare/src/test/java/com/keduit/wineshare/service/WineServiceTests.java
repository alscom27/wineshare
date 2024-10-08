package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.entity.Wine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Transactional
@SpringBootTest
public class WineServiceTests {

  @Autowired
  private WineService wineService;

  @Test
  @DisplayName("와인 등록 테스트")
  public void saveWineTest() {
    Wine wine = new Wine();


    wine.setWineName("Almaviva 2007");
    wine.setWineType(WineType.SPARKLING);
    wine.setCountry("프랑스");
    wine.setRegion("샹빠뉴");
    wine.setPrice(200000);




    Wine savedWine = wineService.saveWine(wine);


    System.out.println(savedWine);

    assertEquals(wine.getWineName(), savedWine.getWineName());

  }


}
