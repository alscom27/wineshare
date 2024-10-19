package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.dto.*;
import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WineController {

  private final WineService wineService;
  private final MemberService memberService;
  private final WineRepository wineRepository;
  private final WineDevelopService wineDevelopService;
  private final WineReviewService wineReviewService;
  private final AromaWheelService aromaWheelService;
  private final FoodPairingService foodPairingService;
  private final CellarService cellarService;
  private final ImgFileService imgFileService;

  // 와인 목록
  @GetMapping({"/list", "/list/{page}"})
  public String wineList(WineSearchDTO wineSearchDTO,
                         @PathVariable("page") Optional<Integer> page,
                         Model model, Principal principal) {
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);


    // 사용자가 로그인했는지 확인하고, 로그인한 경우 셀러에 있는지 확인
    if (principal != null) {
      String email = principal.getName();
      List<Boolean> isInCellarList = new ArrayList<>();

      for (WineDTO wine : wines) {
        boolean isInCellar = cellarService.isWineInCellar(wine.getId(), email);
        isInCellarList.add(isInCellar);
      }

      model.addAttribute("isInCellarList", isInCellarList);
    } else {
      // 비로그인 상태에서는 모든 isInCellar 값을 false로 설정
      List<Boolean> isInCellarList = Collections.nCopies(wines.getContent().size(), false);
      model.addAttribute("isInCellarList", isInCellarList);
    }

    model.addAttribute("wines", wines);
    model.addAttribute("wineSearchDTO", wineSearchDTO);
    model.addAttribute("maxPage", 5);


    return "wine/wineList";
  }

  // 리로드 시 사용
  @GetMapping({"/list/json", "/list/json/{page}"})
  @ResponseBody // JSON을 반환하기 위해 추가
  public ResponseEntity<Map<String, Object>> wineListJson(WineSearchDTO wineSearchDTO,
                                                          @PathVariable("page") Optional<Integer> page,
                                                          Principal principal) {
    Pageable pageable = PageRequest.of(page.orElse(0), 12);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("wines", wines.getContent());
    response.put("totalPages", wines.getTotalPages());
    response.put("currentPage", wines.getNumber());
    response.put("maxPage", 5); // 필요에 따라 조정

    // 사용자가 소장하고 있는지 확인
    List<Boolean> isInCellarList = new ArrayList<>();
    for (WineDTO wineDTO : wines.getContent()) {
      boolean isinCellar = false;
      if (principal != null) {
        isinCellar = cellarService.isWineInCellar(wineDTO.getId(), principal.getName());
      }
      isInCellarList.add(isinCellar);
    }
    response.put("isInCellar", isInCellarList); // 각 와인에 대한 소장 여부 추가

    return ResponseEntity.ok(response);
  }


  // 레스트..임..
  // 와인목록 가격필터에 최소, 최댓값을 주기 위한 메소드
  @GetMapping("/priceRange")
  @ResponseBody
  public PriceRangeDTO getPriceRange() {
    Double minPrice = wineRepository.findMinPrice();
    Double maxPrice = wineRepository.findMaxPrice();
    return new PriceRangeDTO(minPrice, maxPrice);
  }


  // 와인등록(폼 페이지로)
  // /wines/new 로 들어가면 wineForm 페이지로 이동
  @GetMapping("/new")
  public String wineForm(Model model) {

    model.addAttribute("wineDTO", new WineDTO());

    return "wine/wineForm";
  }


  // 와인등록(ajax)
  @PostMapping("/new")
  public ResponseEntity<Map<String, ?>> saveWine(@Valid @ModelAttribute WineDTO wineDTO,
                                                 @RequestParam(value = "wineImgFile") MultipartFile wineImgFile,
                                                 Principal principal) throws IOException {
    Member member = memberService.findByEmail(principal.getName());
    Map<String, Long> response = new HashMap<>();
    try {
      // 와인 생성
      if (wineImgFile != null && !wineImgFile.isEmpty()) {
        try {
          // 파일을 선택한 경우
          String wineImg = imgFileService.saveWineImg(wineDTO, wineImgFile);
          wineDTO.setWineImg(wineImg);
        } catch (Exception e) {
          // 500 INTERNAL SERVER ERROR 응답
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Map.of("error","이미지 저장 중 오류가 발생했습니다. 다시 시도해 주세요."));
        }
      } else {
        return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일을 선택해 주세요."));
      }
      Wine wine = Wine.createWine(wineDTO, member);
      Wine savedWine = wineService.saveWine(wine);
      response.put("wineId", savedWine.getId());
      return ResponseEntity.ok(response);
    } catch (IllegalStateException e) {
      // 에러 발생 시 400 BAD REQUEST와 에러 메시지 반환
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      // 일반적인 오류에 대해 500 INTERNAL SERVER ERROR와 에러 메시지 반환
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "와인 저장 중 오류가 발생했습니다."));
    }
  }

  // 와인상세
  @GetMapping("/wine/{wineId}")
  public String wineDetail(@PathVariable("wineId") Long wineId, Model model, Principal principal) {
    Wine wine = wineService.getWineById(wineId);
    WineDTO wineDTO = wineService.getWineDetail(wineId);
    // 카운트, 평균낸 DTO
    WineDevelopDTO wineDevelopCount = wineDevelopService.getCountDevelop(wine);
    // 리뷰 별점 평균 추가
    WineReviewDTO wineReviewRating = wineReviewService.getCountReviewRating(wine);

    Member member = null;
    // 로그인한 멤버 아이디 가져가기
    if (principal == null) {
      member = null;
    } else {
      member = memberService.findByEmail(principal.getName());
    }
    // 아로마 객체..
//    AromaWheel aromaOne = aromaWheelService.getAromaWheelByAroma(wineDevelopCount.getAromaOne());
//    AromaWheel aromaTwo = aromaWheelService.getAromaWheelByAroma(wineDevelopCount.getAromaTwo());
//    FoodPairing foodOne = foodPairingService.getFoodPairingByFood(wineDevelopCount.getFoodOne());
//    FoodPairing foodTwo = foodPairingService.getFoodPairingByFood(wineDevelopCount.getFoodTwo());
    AromaWheel aromaOne = null;
    AromaWheel aromaTwo = null;
    FoodPairing foodOne = null;
    FoodPairing foodTwo = null;
    if (wineDevelopCount != null) {
      String aromaOneValue = wineDevelopCount.getAromaOne();
      String aromaTwoValue = wineDevelopCount.getAromaTwo();
      String foodOneValue = wineDevelopCount.getFoodOne();
      String foodTwoValue = wineDevelopCount.getFoodTwo();
      // AromaWheel 조회
      if (aromaOneValue != null) {
        aromaOne = aromaWheelService.getAromaWheelByAroma(aromaOneValue);
      } else {
        aromaOne = new AromaWheel();
        aromaOne.setAroma("Unknown");
      }
      if (aromaTwoValue != null) {
        aromaTwo = aromaWheelService.getAromaWheelByAroma(aromaTwoValue);
      } else {
        aromaTwo = new AromaWheel();
        aromaTwo.setAroma("Unknown");
      }
      // FoodPairing 조회
      if (foodOneValue != null) {
        foodOne = foodPairingService.getFoodPairingByFood(foodOneValue);
      } else {
        foodOne = new FoodPairing();
        foodOne.setFood("Unknown");
      }
      if (foodTwoValue != null) {
        foodTwo = foodPairingService.getFoodPairingByFood(foodTwoValue);
      } else {
        foodTwo = new FoodPairing();
        foodTwo.setFood("Unknown");
      }

    }

    // 유사한 와인 리스트 찾아주기(조건 네개)
    List<Wine> similarWineList = wineService.getSimilarWines(wine);

    // 로그인한 멤버의 셀러에 있는 와인 찾아오기
    boolean isinCellar = false;
    if (principal != null) {
      isinCellar = cellarService.isWineInCellar(wineId, principal.getName());
    }

    model.addAttribute("wine", wineDTO);
    model.addAttribute("loginUser", member);
    model.addAttribute("wineDevelopCount", wineDevelopCount);
    model.addAttribute("wineReviewRating", wineReviewRating);
    model.addAttribute("aromaOne", aromaOne);
    model.addAttribute("aromaTwo", aromaTwo);
    model.addAttribute("foodOne", foodOne);
    model.addAttribute("foodTwo", foodTwo);
    model.addAttribute("similarWineList", similarWineList);
    model.addAttribute("isInCellar", isinCellar);
    return "wine/wineDetail";
  }


  // 관리자, 전문가용 수정폼 가기
  @GetMapping("/wine/get/{wineId}")
  public String adminWineDetail(@PathVariable("wineId") Long wineId, Model model) {
    WineDTO wineDTO = wineService.getWineDetail(wineId);
    model.addAttribute("wine", wineDTO);
    return "wine/wineModify";
  }

  @DeleteMapping("/{wineId}")
  @ResponseBody
  public ResponseEntity<String> deleteWine(@PathVariable("wineId") Long wineId,
                                           Principal principal) {
    Member member = memberService.findByEmail(principal.getName());
    if (member.getMemberType() != MemberType.ADMIN) {
      return new ResponseEntity<>("와인 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    wineService.remove(wineId);
    return new ResponseEntity<>("와인이 삭제되었습니다.", HttpStatus.OK);
  }



}

