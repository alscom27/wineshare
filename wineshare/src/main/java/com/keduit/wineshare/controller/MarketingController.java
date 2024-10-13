package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MarketingRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.MarketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/marketings")
@RequiredArgsConstructor
public class MarketingController {

  private final MarketingService marketingService;
  private final MarketingRepository marketingRepository;
  private final MemberRepository memberRepository;

  // 업장별 목록
  @GetMapping({"/{marketCategory}/list/{page}", "/{marketCategory}/list"})
  public String getMarketingListByCategory(@PathVariable("marketCategory")MarketCategory marketCategory,
                                           @PathVariable("page")Optional<Integer> page,
                                           MarketingSearchDTO marketingSearchDTO,
                                           Model model){
    Pageable pageable = PageRequest.of(page.orElse(0), 5);

    Page<Marketing>  marketings = marketingService.getMarketingPageByMarketCategory(marketingSearchDTO, marketCategory, pageable);

    model.addAttribute("marketings", marketings);
    model.addAttribute("marketCategory", marketCategory);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);
    return "marketing/marketingList";
  }

  // 업장별 등록으로 보내기
  @GetMapping({"/{marketCategory}/new"})
  public String marketingForm(@PathVariable("marketCategory") MarketCategory marketCategory,
                              Model model){
    model.addAttribute("marketingDTO", new MarketingDTO());
    return "marketing/marketingForm";
  }

  // 업장별 등록
  @PostMapping({"/{marketCategory}/new"})
  public String marketingNew(@PathVariable("marketCategory") MarketCategory marketCategory,
                             @Valid MarketingDTO marketingDTO,
                             BindingResult bindingResult,
                             Principal principal,
                             @RequestParam("marketImg")MultipartFile marketImg,
                             Model model) throws IOException {
    if(bindingResult.hasErrors()){
      return "marketing/marketingForm";
    }
      // 사용자 정보 가져오기
      Member member = memberRepository.findByEmail(principal.getName());

      // 닉네임 설정
      marketingDTO.setOwnerNickname(member.getNickname());
      marketingDTO.setMarketCategory(marketCategory);

      marketingService.saveMarketing(marketingDTO);
      return "redirect:/marketing/" + marketCategory + "/0";
  }

  // 상세조회는 없음 바로 링크타고 날아가면됨

  // 업장별 마케팅 수정
  @PostMapping({"/{marketCategory}/modify/{marketingId}"})
  public String marketingUpdate(@PathVariable("marketCategory") MarketCategory marketCategory,
                                @PathVariable("marketingId") Long marketingId,
                                @Valid MarketingDTO marketingDTO,
                                BindingResult bindingResult,
                                @RequestParam("marketImg") MultipartFile marketImg,
                                Model model) throws  IOException{
    if(bindingResult.hasErrors()){
      return "marketing/marketingForm";
    }

    marketingDTO.setId(marketingId);  // 수정할 애
    marketingDTO.setMarketImgFile(marketImg); // 업로드할 이미지 파일 설정

    marketingService.updateMarketing(marketingDTO);
    return "redirect:/marketing/" + marketCategory + "/0";
  }

  // 마케팅 삭제
  @PostMapping({"/{marketCategory}/remove/{marketingId}"})
  public String marketingDelete(@PathVariable("marketCategory") MarketCategory marketCategory,
                                @PathVariable("marketingId") Long marketingId){
    marketingService.deleteMarketing(marketingId);
    return "redirect:/marketing/" + marketCategory + "/0";
  }

  // 행사중인거 조회
  @GetMapping({"/{eventOrNot}/{page}"})
  public String getMarketingListByEvent(@PathVariable("eventOrNot") EventOrNot eventOrNot,
                                        @PathVariable("page") Optional<Integer> page,
                                        MarketingSearchDTO marketingSearchDTO,
                                        Model model){
    Pageable pageable = PageRequest.of(page.orElse(0), 5);

    eventOrNot = EventOrNot.ON;
    Page<Marketing> marketings = marketingService.getMarketingPageByEventOrNot(marketingSearchDTO, eventOrNot, pageable);

    model.addAttribute("marketings", marketings);
    model.addAttribute("eventOrNot", eventOrNot);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);
    return "event/promotionList";
  }

}
