package com.keduit.wineshare.service;

import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.repository.AromaWheelRepository;
import com.keduit.wineshare.repository.FoodPairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataInitializer {

  // 초기 페어링, 아로마 데이터 만들어주는 클래스
  // 푸드페어링이랑 아로마휠 넣는부분 메소드화 시키고,
  // 초기 데이터 만들 때 멤버도 하나 만들어서 초기데이터로 들어가는 모든 와인에 해당 멤버가 들어갈수 있게 해줘야함
  // 가격도 랜덤 때려서 넣을까..?
  // 서비스에 있는 와인 파싱하는애도 이쪽으로 옮기기

  private final FoodPairingRepository foodPairingRepository;
  private final AromaWheelRepository aromaWheelRepository;

  @Autowired
  public DataInitializer(FoodPairingRepository foodPairingRepository, AromaWheelRepository aromaWheelRepository) {
    this.foodPairingRepository = foodPairingRepository;
    this.aromaWheelRepository = aromaWheelRepository;
  }

  @PostConstruct // 최초 실행시 메소드 실행해줌
  public void initFoodPairingAndAromaWheel() {
    // 한개라도 데이터 존재하면 더이상 실행하지 않음
    if (foodPairingRepository.count() == 0) {
      createFoodPairing("Beef", "beef_image.jpg");
      createFoodPairing("Chicken", "chicken_image.jpg");
      createFoodPairing("Pork", "pork_image.jpg");
      createFoodPairing("Mushroom", "mushroom_image.jpg");
      createFoodPairing("Charcuterie", "charcuterie_image.jpg");
      createFoodPairing("Cheese", "cheese_image.jpg");
      createFoodPairing("Pasta", "pasta_image.jpg");
      createFoodPairing("Spicy", "Spicy_image.jpg");
      createFoodPairing("Snack", "snack_image.jpg");
      createFoodPairing("Shell", "shell_image.jpg");
      createFoodPairing("Fish", "fish_image.jpg");
      createFoodPairing("Vegetable", "vegetable_image.jpg");
    }
    if (aromaWheelRepository.count() == 0) {
      createAromaWheel("Citrus", "레몬, 라임, 자몽", "citrus_image.jpg");
      createAromaWheel("TreeFruit", "배, 사과, 풋사과", "treefruit_image.jpg");
      createAromaWheel("StoneFruit", "복숭아, 자두, 살구", "stonefruit_image.jpg");
      createAromaWheel("TropicalFruit", "구아바, 패션프루트, 리치", "tropicalfruit_image.jpg");
      createAromaWheel("Berry", "산딸기, 딸기, 커런트", "berry_image.jpg");
      createAromaWheel("Floral", "장미, 자스민, 라벤더", "floral_image.jpg");
      createAromaWheel("Herb", "딜, 타임, 바질", "herb_image.jpg");
      createAromaWheel("Vegetable", "피망, 토마토, 아스파라거스", "vegetable_image.jpg");
      createAromaWheel("Fermented", "빵, 버터, 효모", "fermented_image.jpg");
      createAromaWheel("Roasted", "카라멜, 초콜렛, 커피, 토스트", "roasted_image.jpg");
      createAromaWheel("Spice", "바닐라, 후추, 정향", "spice_image.jpg");
      createAromaWheel("Nuts", "아몬드, 헤이즐넛, 피칸", "nuts_image.jpg");
      createAromaWheel("Tree", "백단목, 백향목, 솔향", "tree_image.jpg");
      createAromaWheel("Sugar", "꿀, 모과", "sugar_image.jpg");
      createAromaWheel("Fungi", "버섯, 이끼, 송로", "fungi_image.jpg");
    }
  }

  private FoodPairing createFoodPairing(String food, String foodImg) {
    FoodPairing foodPairing = new FoodPairing();
    foodPairing.setFood(food);
    foodPairing.setFoodImg(foodImg);
    return foodPairingRepository.save(foodPairing);
  }

  private AromaWheel createAromaWheel(String aroma, String aromaValue, String aromaImg) {
    AromaWheel aromaWheel = new AromaWheel();
    aromaWheel.setAroma(aroma);
    aromaWheel.setAromaValue(aromaValue);
    aromaWheel.setAromaImg(aromaImg);
    return aromaWheelRepository.save(aromaWheel);
  }

}


