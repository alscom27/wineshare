package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import com.keduit.wineshare.repository.AromaWheelRepository;
import com.keduit.wineshare.repository.FoodPairingRepository;
import com.keduit.wineshare.repository.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WineDevelopServiceTests {

  @Autowired
  private WineDevelopService wineDevelopService;
  @Autowired
  private WineRepository wineRepository;
  @Autowired
  private FoodPairingRepository foodPairingRepository;
  @Autowired
  private AromaWheelRepository aromaWheelRepository;


  private Wine wine;

  @BeforeEach // 테스트 메소드 실행 이전에 수행, 테스트에 필요한 데이터들을 미리 만들어주도록 함
  public void setUp() {
    wine = new Wine();
    wine.setWineName("Test Wine");
    wine.setWineType(WineType.RED);
    wine.setCountry("France");
    wine.setRegion("Bordeaux");
    wine.setPrice(20000);
    wineRepository.save(wine);

    // 테스트용 FoodPairing 객체를 저장
    FoodPairing foodPairing1 = new FoodPairing();
    foodPairing1.setFood("Beef");
    foodPairing1.setFoodImg("소사진");
    foodPairingRepository.save(foodPairing1);

    FoodPairing foodPairing2 = new FoodPairing();
    foodPairing2.setFood("Chicken");
    foodPairing2.setFoodImg("닭사진");
    foodPairingRepository.save(foodPairing2);

    // 테스트용 AromaWheel 객체를 저장
    AromaWheel aromaWheel1 = new AromaWheel();
    aromaWheel1.setAroma("Citrus");
    aromaWheel1.setAromaValue("레몬, 라임, 오렌지");
    aromaWheelRepository.save(aromaWheel1);

    AromaWheel aromaWheel2 = new AromaWheel();
    aromaWheel2.setAroma("Flower");
    aromaWheel2.setAromaValue("장미, 자스민, 라벤더");
    aromaWheelRepository.save(aromaWheel2);
  }

  @Test
  public void testSaveWineDevelop() {

    System.out.println(wine);

    // WineDevelopDTO 생성
    WineDevelopDTO wineDevelopDTO = new WineDevelopDTO();
    wineDevelopDTO.setExpertRating(4.5);
    wineDevelopDTO.setExpertComment("Excellent wine!");
    wineDevelopDTO.setSweetness(2.0);
    wineDevelopDTO.setAcidity(1.5);
    wineDevelopDTO.setBody(3.0);
    wineDevelopDTO.setTannin(2.5);
    wineDevelopDTO.setFizz(0.5);
    wineDevelopDTO.setAromaOne("Citrus");
    wineDevelopDTO.setAromaTwo("Flower");
    wineDevelopDTO.setFoodOne("Chicken");
    wineDevelopDTO.setFoodTwo("Beef");
    wineDevelopDTO.setWindId(wine.getId());

    // WineDevelopDTO -> WineDevelop entity 저장
    wineDevelopService.saveWineDevelop(wineDevelopDTO);

    // 저장된 WineDevelop 확인하기
    List<WineDevelop> wineDevelops = wineDevelopService.findAllByWine(wine);

    assertThat(wineDevelops).isNotEmpty();
    WineDevelop savedWineDevelop = wineDevelops.get(0);
    String foodImg = foodPairingRepository.findFoodImgByFood(savedWineDevelop.getFoodOne());
    System.out.println("와인 이름 : " + wineDevelops.get(0).getWine().getWineName());
    System.out.println("전문가 평점 : " + wineDevelops.get(0).getExpertRating());
    System.out.println("어울리는 음식 : " + wineDevelops.get(0).getFoodOne());
    System.out.println("음식 이미지 : " + foodImg);
    System.out.println("아로마 : " + wineDevelops.get(0).getAromaTwo());
    System.out.println("아로마 상세 : " + aromaWheelRepository.findAromaValueByAroma(wineDevelops.get(0).getAromaTwo()));

  }
}
