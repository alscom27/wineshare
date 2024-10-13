package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.PriceRangeDTO;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.service.MemberService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WineController {

  private final WineService wineService;
  private final MemberService memberService;
  private final WineRepository wineRepository;

  // 와인 목록
  @GetMapping({"/list", "/list/{page}"})
  public String wineList(WineSearchDTO wineSearchDTO,
                         @PathVariable("page") Optional<Integer> page,
                         Model model) {
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);
    model.addAttribute("wines", wines);
    model.addAttribute("wineSearchDTO", wineSearchDTO);
    model.addAttribute("maxPage", 5);


    return "wine/wineList";
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
  public String wineDetail(Model model, @PathVariable("wineId") Long wineId) {
    WineDTO wineDTO = wineService.getWineDetail(wineId);
    model.addAttribute("wine", wineDTO);
    return "wine/wineDetail";
  }



}

