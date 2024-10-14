package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.*;
import com.keduit.wineshare.repository.AromaWheelRepository;
import com.keduit.wineshare.repository.FoodPairingRepository;
import com.keduit.wineshare.repository.MemberRepository;
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
  private Member member;
  @Autowired
  private MemberRepository memberRepository;

  private Member createMember(String email) {
    Member member = new Member();
    member.setEmail(email);
    return memberRepository.save(member);
  }

  private Wine createWine(String wineName, WineType wineType, String country, String region, int price, Member member) {
    Wine wine = new Wine();
    wine.setWineName(wineName);
    wine.setWineType(wineType);
    wine.setCountry(country);
    wine.setRegion(region);
    wine.setPrice(price);
    wine.setMember(member);
    return wineRepository.save(wine);
  }

  private FoodPairing createFoodPairing(String food, String foodImg) {
    FoodPairing foodPairing = new FoodPairing();
    foodPairing.setFood(food);
    foodPairing.setFoodImg(foodImg);
    return foodPairingRepository.save(foodPairing);
  }

  private AromaWheel createAromaWheel(String aroma, String aromaValue) {
    AromaWheel aromaWheel = new AromaWheel();
    aromaWheel.setAroma(aroma);
    aromaWheel.setAromaValue(aromaValue);
    return aromaWheelRepository.save(aromaWheel);
  }
  @BeforeEach // 테스트 메소드 실행 이전에 수행, 테스트에 필요한 데이터들을 미리 만들어주도록 함
  public void setUp() {
    member = createMember("test@teeet.com");

    wine = createWine("Test Wine", WineType.RED, "France", "Bordeaux", 20000, member);

    createFoodPairing("Beef_test", "소사진");
    createFoodPairing("Chicken_test", "닭사진");

    createAromaWheel("Citrus_test", "레몬, 라임, 오렌지");
    createAromaWheel("Flower_test", "장미, 자스민, 라벤더");
  }

  @Test
  public void testSaveWineDevelop() {
    // WineDevelopDTO 생성
    WineDevelopDTO wineDevelopDTO = new WineDevelopDTO();
    wineDevelopDTO.setExpertRating(4.5);
    wineDevelopDTO.setExpertComment("Excellent wine!");
    wineDevelopDTO.setSweetness(2.0);
    wineDevelopDTO.setAcidity(1.5);
    wineDevelopDTO.setBody(3.0);
    wineDevelopDTO.setTannin(2.5);
    wineDevelopDTO.setFizz(0.5);
    wineDevelopDTO.setAromaOne("Citrus_test");
    wineDevelopDTO.setAromaTwo("Flower_test");
    wineDevelopDTO.setFoodOne("Chicken_test");
    wineDevelopDTO.setFoodTwo("Beef_test");
    wineDevelopDTO.setWindId(wine.getId());
    wineDevelopDTO.setMemberId(member.getId());

    // WineDevelopDTO -> WineDevelop entity 저장
    wineDevelopService.saveWineDevelop(wineDevelopDTO, member.getEmail());

    // 저장된 WineDevelop 확인하기
    List<WineDevelopDTO> wineDevelops = wineDevelopService.findAllByWine(wine);


  }
}
