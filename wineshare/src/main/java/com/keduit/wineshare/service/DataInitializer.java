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
      FoodPairing foodPairing1 = new FoodPairing();
      foodPairing1.setFood("Beef");
      foodPairing1.setFoodImg("beef_image.jpg");
      foodPairingRepository.save(foodPairing1);
      FoodPairing foodPairing2 = new FoodPairing();
      foodPairing2.setFood("Chicken");
      foodPairing2.setFoodImg("chicken_image.jpg");
      foodPairingRepository.save(foodPairing2);
      FoodPairing foodPairing3 = new FoodPairing();
      foodPairing3.setFood("Pork");
      foodPairing3.setFoodImg("pork_image.jpg");
      foodPairingRepository.save(foodPairing3);
      FoodPairing foodPairing4 = new FoodPairing();
      foodPairing4.setFood("Mushroom");
      foodPairing4.setFoodImg("mushroom_image.jpg");
      foodPairingRepository.save(foodPairing4);
      FoodPairing foodPairing5 = new FoodPairing();
      foodPairing5.setFood("Charcuterie");
      foodPairing5.setFoodImg("charcuterie_image.jpg");
      foodPairingRepository.save(foodPairing5);
      FoodPairing foodPairing6 = new FoodPairing();
      foodPairing6.setFood("Cheese");
      foodPairing6.setFoodImg("cheese_image.jpg");
      foodPairingRepository.save(foodPairing6);
      FoodPairing foodPairing7 = new FoodPairing();
      foodPairing7.setFood("Pasta");
      foodPairing7.setFoodImg("pasta_image.jpg");
      foodPairingRepository.save(foodPairing7);
      FoodPairing foodPairing8 = new FoodPairing();
      foodPairing8.setFood("Spicy");
      foodPairing8.setFoodImg("Spicy_image.jpg");
      foodPairingRepository.save(foodPairing8);
      FoodPairing foodPairing9 = new FoodPairing();
      foodPairing9.setFood("Snack");
      foodPairing9.setFoodImg("snack_image.jpg");
      foodPairingRepository.save(foodPairing9);
      FoodPairing foodPairing10 = new FoodPairing();
      foodPairing10.setFood("Shell");
      foodPairing10.setFoodImg("shell_image.jpg");
      foodPairingRepository.save(foodPairing10);
      FoodPairing foodPairing11 = new FoodPairing();
      foodPairing11.setFood("Fish");
      foodPairing11.setFoodImg("fish_image.jpg");
      foodPairingRepository.save(foodPairing11);
      FoodPairing foodPairing12 = new FoodPairing();
      foodPairing12.setFood("Vegetable");
      foodPairing12.setFoodImg("vegetable_image.jpg");
      foodPairingRepository.save(foodPairing12);


    }
    if (aromaWheelRepository.count() == 0) {
      AromaWheel aromaWheel1 = new AromaWheel();
      aromaWheel1.setAroma("Citrus");
      aromaWheel1.setAromaValue("레몬, 라임, 자몽");
      aromaWheelRepository.save(aromaWheel1);
      AromaWheel aromaWheel2 = new AromaWheel();
      aromaWheel2.setAroma("TreeFruit");
      aromaWheel2.setAromaValue("배, 사과, 풋사과");
      aromaWheelRepository.save(aromaWheel2);
      AromaWheel aromaWheel3 = new AromaWheel();
      aromaWheel3.setAroma("StoneFruit");
      aromaWheel3.setAromaValue("복숭아, 자두, 살구");
      aromaWheelRepository.save(aromaWheel3);
      AromaWheel aromaWheel4 = new AromaWheel();
      aromaWheel4.setAroma("TropicalFruit");
      aromaWheel4.setAromaValue("구아바, 패션프루트, 리치");
      aromaWheelRepository.save(aromaWheel4);
      AromaWheel aromaWheel5 = new AromaWheel();
      aromaWheel5.setAroma("Berry");
      aromaWheel5.setAromaValue("산딸기, 딸기, 커런트");
      aromaWheelRepository.save(aromaWheel5);
      AromaWheel aromaWheel6 = new AromaWheel();
      aromaWheel6.setAroma("Fruit");
      aromaWheel6.setAromaValue("장미, 자스민, 라벤더");
      aromaWheelRepository.save(aromaWheel6);
      AromaWheel aromaWheel7 = new AromaWheel();
      aromaWheel7.setAroma("Herb");
      aromaWheel7.setAromaValue("딜, 타임, 바질");
      aromaWheelRepository.save(aromaWheel7);
      AromaWheel aromaWheel8 = new AromaWheel();
      aromaWheel8.setAroma("Vegetable");
      aromaWheel8.setAromaValue("피망, 토마토, 아스파라거스");
      aromaWheelRepository.save(aromaWheel8);
      AromaWheel aromaWheel9 = new AromaWheel();
      aromaWheel9.setAroma("Fermented");
      aromaWheel9.setAromaValue("빵, 버터, 효모");
      aromaWheelRepository.save(aromaWheel9);
      AromaWheel aromaWheel10 = new AromaWheel();
      aromaWheel10.setAroma("Roasted");
      aromaWheel10.setAromaValue("카라멜, 초콜렛, 커피, 토스트");
      aromaWheelRepository.save(aromaWheel10);
      AromaWheel aromaWheel11 = new AromaWheel();
      aromaWheel11.setAroma("Spice");
      aromaWheel11.setAromaValue("바닐라, 후추, 정향");
      aromaWheelRepository.save(aromaWheel11);
      AromaWheel aromaWheel12 = new AromaWheel();
      aromaWheel12.setAroma("Nuts");
      aromaWheel12.setAromaValue("아몬드, 헤이즐넛, 피칸");
      aromaWheelRepository.save(aromaWheel12);
      AromaWheel aromaWheel13 = new AromaWheel();
      aromaWheel13.setAroma("Tree");
      aromaWheel13.setAromaValue("백단목, 백향목, 솔향");
      aromaWheelRepository.save(aromaWheel13);
      AromaWheel aromaWheel14 = new AromaWheel();
      aromaWheel14.setAroma("Sugar");
      aromaWheel14.setAromaValue("꿀, 모과");
      aromaWheelRepository.save(aromaWheel14);
      AromaWheel aromaWheel15 = new AromaWheel();
      aromaWheel15.setAroma("Fungi");
      aromaWheel15.setAromaValue("버섯, 이끼, 송로");
      aromaWheelRepository.save(aromaWheel15);


    }
  }

}
