package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.AromaWheelRepository;
import com.keduit.wineshare.repository.FoodPairingRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataInitializer {

  // 초기 페어링, 아로마 데이터 만들어주는 클래스
  // 푸드페어링이랑 아로마휠 넣는부분 메소드화 시키고,
  // 초기 데이터 만들 때 멤버도 하나 만들어서 초기데이터로 들어가는 모든 와인에 해당 멤버가 들어갈수 있게 해줘야함
  // 가격도 랜덤 때려서 넣을까..?
  // 서비스에 있는 와인 파싱하는애도 이쪽으로 옮기기

  private final FoodPairingRepository foodPairingRepository;
  private final AromaWheelRepository aromaWheelRepository;
  private final WineRepository wineRepository;
  private final WineDevelopService wineDevelopService;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final WineReviewService wineReviewService;

  @Autowired
  public DataInitializer(FoodPairingRepository foodPairingRepository, AromaWheelRepository aromaWheelRepository, WineRepository wineRepository, WineDevelopService wineDevelopService, MemberRepository memberRepository, PasswordEncoder passwordEncoder, WineReviewService wineReviewService) {
    this.foodPairingRepository = foodPairingRepository;
    this.aromaWheelRepository = aromaWheelRepository;
    this.wineRepository = wineRepository;
    this.wineDevelopService = wineDevelopService;
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.wineReviewService = wineReviewService;
  }

  @PostConstruct // 최초 실행시 메소드 실행해줌
  public void initFoodPairingAndAromaWheel() {
    // 한개라도 데이터 존재하면 더이상 실행하지 않음
    if (foodPairingRepository.count() == 0) {
      createFoodPairing("Beef", "/wineshare-img/wineshare-food/beef.png");
      createFoodPairing("Chicken", "/wineshare-img/wineshare-food/chicken.png");
      createFoodPairing("Pork", "/wineshare-img/wineshare-food/pork.png");
      createFoodPairing("Mushroom", "/wineshare-img/wineshare-food/mushroom.png");
      createFoodPairing("Charcuterie", "/wineshare-img/wineshare-food/charcuterie.png");
      createFoodPairing("Cheese", "/wineshare-img/wineshare-food/cheese.png");
      createFoodPairing("Pasta", "/wineshare-img/wineshare-food/pasta.png");
      createFoodPairing("Spicy", "/wineshare-img/wineshare-food/spicy.png");
      createFoodPairing("Snack", "/wineshare-img/wineshare-food/snack.png");
      createFoodPairing("Shell", "/wineshare-img/wineshare-food/shell.png");
      createFoodPairing("Fish", "/wineshare-img/wineshare-food/fish.png");
      createFoodPairing("Vegetable", "/wineshare-img/wineshare-food/vegetable.png");
    }
    if (aromaWheelRepository.count() == 0) {
      createAromaWheel("Citrus", "레몬 라임 자몽", "/wineshare-img/wineshare-aroma/citrus.png");
      createAromaWheel("Tree Fruit", "배 사과 풋사과", "/wineshare-img/wineshare-aroma/treefruit.png");
      createAromaWheel("Stone Fruit", "복숭아 자두 살구", "/wineshare-img/wineshare-aroma/stonefruit.png");
      createAromaWheel("Tropical Fruit", "구아바 패션프루트 리치", "/wineshare-img/wineshare-aroma/tropicalfruit.png");
      createAromaWheel("Berry", "산딸기 딸기 커런트", "/wineshare-img/wineshare-aroma/berry.png");
      createAromaWheel("Floral", "장미 자스민 라벤더", "/wineshare-img/wineshare-aroma/floral.png");
      createAromaWheel("Herb", "딜 타임 바질", "/wineshare-img/wineshare-aroma/herb.png");
      createAromaWheel("Vegetable", "피망 토마토 아스파라거스", "/wineshare-img/wineshare-aroma/vegetable.png");
      createAromaWheel("Fermented", "빵 버터 효모", "/wineshare-img/wineshare-aroma/fermented.png");
      createAromaWheel("Roasted", "카라멜 초콜렛 커피 토스트", "/wineshare-img/wineshare-aroma/roasted.png");
      createAromaWheel("Spice", "바닐라 후추 정향", "/wineshare-img/wineshare-aroma/spice.png");
      createAromaWheel("Nuts", "아몬드 헤이즐넛 피칸", "/wineshare-img/wineshare-aroma/nuts.png");
      createAromaWheel("Tree", "백단목 백향목 솔향", "/wineshare-img/wineshare-aroma/tree.png");
      createAromaWheel("Honey", "꿀 모과", "/wineshare-img/wineshare-aroma/honey.png");
      createAromaWheel("Fungi", "버섯 이끼 송로", "/wineshare-img/wineshare-aroma/fungi.png");
    }
    if (wineRepository.count() == 0) {
      initParsing();
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

  // 파싱 메소드 만들어보자
  // @PostConstruct 어노테이션이 메소드를 애플리케이션 최초 실행 시 한번 실행 시켜줌
  public void initParsing() {
    String[] wineTypeList = {"reds", "whites", "rose", "port", "dessert", "sparkling"};

    // 테스트 멤버 생성
    Member member = createTestMember();

    member = memberRepository.save(member);

    if (wineRepository.count() == 0){ // 데이터 한개라도 있으면 여기서 막힘
      for(String wineType : wineTypeList) {
        try {
          // JSON 을 InputStream 으로 read
          URL url = new URL("https://api.sampleapis.com/wines/" + wineType);
          try(InputStream inputStream = url.openStream()) {
            List<Wine> wines = parseWine(inputStream, wineType, member);
            wineRepository.saveAll(wines);

            // 저장된 각 와인에 랜덤한 와인디벨롬 생성, 저장
            for (Wine wine : wines) {
              int randomCount = getRandomDevelopCount(15, 20);
              for (int i = 0; i < randomCount; i++) {
                WineDevelopDTO wineDevelopDTO = createRandomWineDevelop(wine, member);
                WineReviewDTO wineReviewDTO = createRandomWineReview(wine, member);
                wineDevelopService.saveWineDevelop(wineDevelopDTO);
                wineReviewService.registerReview(wineReviewDTO);
              }
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  private Member createTestMember() {
    Member member = new Member();
    member.setEmail("WineParsing@init.data");
    member.setName("olimwoojin");
    member.setNickname("오리무진");
    String password = passwordEncoder.encode("olimwoojin");
    member.setPassword(password);
    member.setPhoneNumber("000-0000-0000");
    member.setRRN("000000-0000000");
    member.setMemberType(MemberType.ADMIN);
    member.setWithdrawStatus(WithdrawStatus.STAY);
    return member;
  }

  // 파싱해서 리스트 반환
  private List<Wine> parseWine(InputStream inputStream, String wineType, Member member) throws IOException {
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
        wine.setPrice(((int)(Math.random() * ((1500000 - 10000) / 1000 + 1)) * 1000) + 10000);
        wine.setWineType(wineTypeEnum);
        wine.setMember(member);
        wines.add(wine);
        wineNames.add(wineName);
      }
    }
    return wines;
  }

  // 랜덤 범위 설정
  private int getRandomDevelopCount(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min + 1) + min;
  }

  private WineReviewDTO createRandomWineReview(Wine wine, Member member) {
    WineReviewDTO wineReviewDTO = new WineReviewDTO();
    Random random = new Random();
    wineReviewDTO.setWine(wine);
    wineReviewDTO.setMember(member);
    wineReviewDTO.setRegularReview("와인쉐어를 통해 정보를 얻어 마셔본 와인인데 너무 맛있습니다.");
    wineReviewDTO.setRegularRating(1 + (random.nextInt(5)));

    return wineReviewDTO;
  }

  private WineDevelopDTO createRandomWineDevelop(Wine wine, Member member) {
    WineDevelopDTO wineDevelopDTO = new WineDevelopDTO();
    Random random = new Random();
    // Aroma, Food 리스트 가져오기
    List<String> aromas = getAllAromas();
    List<String> foods = getAllFoods();
    // Set을 사용하여 중복된 값을 방지
    Set<String> usedAromas = new HashSet<>();
    Set<String> usedFoods = new HashSet<>();

    wineDevelopDTO.setExpertRating(1 + (random.nextInt(5))); // 1.0 ~ 5.0 사이
    wineDevelopDTO.setExpertComment("마셔본 와인 중에 가장 맛있는 것 같습니다. 모두에게 공유하고 싶어서 평가 남깁니다."); // 랜덤 댓글
    wineDevelopDTO.setSweetness(3 + (random.nextInt(5))); // 0.0 ~ 5.0 사이
    wineDevelopDTO.setAcidity(2 + (random.nextInt(4))); // 0.0 ~ 5.0 사이
    wineDevelopDTO.setBody(1 + (random.nextInt(5))); // 0.0 ~ 5.0 사이
    wineDevelopDTO.setTannin(1 + (random.nextInt(3))); // 0.0 ~ 5.0 사이
    wineDevelopDTO.setFizz(1 + (random.nextInt(5))); // 0.0 ~ 5.0 사이
    wineDevelopDTO.setAromaOne(getRandomItem(aromas, usedAromas));
    wineDevelopDTO.setAromaTwo(getRandomItem(aromas, usedAromas));
    wineDevelopDTO.setFoodOne(getRandomItem(foods, usedFoods));
    wineDevelopDTO.setFoodTwo(getRandomItem(foods, usedFoods));
    wineDevelopDTO.setWineId(wine.getId());
    wineDevelopDTO.setMemberId(member.getId());

    return wineDevelopDTO;
  }

  // 아로마이름 리스트 가져오기
  public List<String> getAllAromas() {
    return aromaWheelRepository.findAll().stream()
        .map(AromaWheel::getAroma)
        .collect(Collectors.toList());
  }

  // 음식이름 리스트 가져오기
  public List<String> getAllFoods() {
    return foodPairingRepository.findAll().stream()
        .map(FoodPairing::getFood)
        .collect(Collectors.toList());
  }

  // 아로마, 푸드 랜덤선택용 메소드
  private String getRandomItem(List<String> items, Set<String> usedItems) {
    Random random = new Random();
    String item;
    do {
      item = items.get(random.nextInt(items.size()));
    } while (usedItems.contains(item)); // 중복체크

    usedItems.add(item);
    return item;
  }

}


