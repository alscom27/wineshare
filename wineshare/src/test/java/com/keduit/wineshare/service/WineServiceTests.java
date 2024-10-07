package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.UserType;
import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.entity.Member;
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


    wine.setWineName("돔페리뇽");
    wine.setWineType(WineType.SPARKLING);
    wine.setCountry("프랑스");
    wine.setRegion("샹빠뉴");
    wine.setPrice(200000);



    assertNotNull(wineService, "wineService가 null입니다."); // wineService null 체크
    Wine savedWine = wineService.saveWine(wine);
    assertNotNull(savedWine, "저장된 와인이 null입니다.");

    System.out.println(savedWine);

    assertEquals(wine.getWineName(), savedWine.getWineName());

  }


}
