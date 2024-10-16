package com.keduit.wineshare.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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


  // 와인등록(db에 등록 후 리스트로)
  // 겟매핑을 통해 들어간 와인폼에서 입력 후 전송 시 포스트매핑으로 들어옴, 와인을 저장함
  @PostMapping("/new")
  public String saveWine(@Valid WineDTO wineDTO, BindingResult bindingResult, Model model, Principal principal) {
    Member member = memberService.findByEmail(principal.getName());
    // WineDTO 유효성 체크, 에러 시 와인폼 페이지를 다시 보여줌
    if (bindingResult.hasErrors()) {
      return "wine/wineForm";
    }

    // 와인 이름 중복 시 등록 불가능하게 처리
    try {
      Wine wine = Wine.createWine(wineDTO, member);
      wineService.saveWine(wine);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage()); // 멤버폼 갈때 모델에 에러메세지 담아감, 가서 알러트할것.
      return "wine/wineForm";
    }
    return "redirect:/wine/wineList";
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
    // 멤버 아이디 가져가기
    if (principal == null) {
      member = null;
    } else {
      member = memberService.findByEmail(principal.getName());
    }


    // 아로마 객체..
    AromaWheel aromaOne = aromaWheelService.getAromaWheelByAroma(wineDevelopCount.getAromaOne());
    AromaWheel aromaTwo = aromaWheelService.getAromaWheelByAroma(wineDevelopCount.getAromaTwo());
    FoodPairing foodOne = foodPairingService.getFoodPairingByFood(wineDevelopCount.getFoodOne());
    FoodPairing foodTwo = foodPairingService.getFoodPairingByFood(wineDevelopCount.getFoodTwo());

    List<Wine> similarWineList = wineService.getSimilarWines(wine);

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



}

