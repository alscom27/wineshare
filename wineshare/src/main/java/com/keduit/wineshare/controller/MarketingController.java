package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MarketingRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.ImgFileService;
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
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/marketings")
@RequiredArgsConstructor
public class MarketingController {

  private final MarketingService marketingService;
  private final MarketingRepository marketingRepository;
  private final MemberRepository memberRepository;
  private final ImgFileService imgFileService;

  // 업장별 목록
  @GetMapping({"/{marketCategory}/list/{page}", "/{marketCategory}/list"})
  public String getMarketingListByCategory(@PathVariable("marketCategory")MarketCategory marketCategory,
                                           @PathVariable("page")Optional<Integer> page,
                                           MarketingSearchDTO marketingSearchDTO,
                                           Model model) {

    // 똑똑이
//    Pageable pageable = PageRequest.of(page.orElse(0), 5);
//
//    Page<Marketing> marketingconfig = marketingService.getMarketingPage(marketingSearchDTO, pageable);
//    System.out.println(marketingconfig);
//    if(marketingconfig.isEmpty()){
//      model.addAttribute("marketingSearchDTO", marketingSearchDTO);
//      model.addAttribute("marketCategory", marketCategory);
//      model.addAttribute("marketingDTOs", new ArrayList<>());
//      model.addAttribute("marketings", marketingconfig);
//      model.addAttribute("maxPage", 5);
//      return "marketing/marketingList";
//    }
//
//
//    Page<Marketing>  marketingPage = marketingService.getMarketingPageByMarketCategory(marketingSearchDTO, marketCategory, pageable);
//
//    System.out.println("Marketing DTOs Size: " + marketingPage.getSize());
//
//    // 닉네임 세팅
//    List<String> emails = marketingPage.stream()
//            .map(Marketing::getRegBy)
//                .collect(Collectors.toList());
//
//    if(emails.isEmpty()){
//      return "redirect:/marketings/"+ marketCategory +"/list";
//    }
//
//    List<Member> members = memberRepository.findByEmailIn(emails);
//
//    Map<String, String> emailToNicknameMap = members.stream()
//            .collect(Collectors.toMap(Member::getEmail, Member::getNickname));
//
//    List<MarketingDTO> marketingDTOs = marketingPage.stream().map(marketing -> {
//      MarketingDTO marketingDTO = new MarketingDTO();
//
//      marketingDTO.setId(marketing.getId());
//      marketingDTO.setMarketingTitle(marketing.getMarketingTitle());
//      marketingDTO.setMarketingContent(marketing.getMarketingContent());
//      marketingDTO.setRegTime(marketing.getRegTime());
//      marketingDTO.setMarketLink(marketing.getMarketLink());
//      marketingDTO.setMarketCategory(marketing.getMarketCategory());
//      marketingDTO.setOwnerNickname(emailToNicknameMap.get(marketing.getRegBy()));
//      marketingDTO.setMarketOriImgName(marketing.getMarketOriImgName());
//      marketingDTO.setMarketImgName(marketing.getMarketImgName());
//      marketingDTO.setMarketImgUrl(marketing.getMarketImgUrl());
//
//      if(StringUtils.equalsIgnoreCase(marketing.getEventOrNot(), "on")){
//        marketingDTO.setEventOrNot(EventOrNot.ON);
//        marketingDTO.setEventContent(marketing.getEventContent());
//        // end 나중에 off로 바꾸기
//      }else if(StringUtils.equalsIgnoreCase(marketing.getEventOrNot(), "end")){
//        marketingDTO.setEventOrNot(EventOrNot.END);
//        marketingDTO.setEventContent("행사 준비중");
//      }
//      return marketingDTO;
//    }).collect(Collectors.toList());
//
//
//    model.addAttribute("marketingDTOs", marketingDTOs);
//    model.addAttribute("marketings", marketingPage);
//    model.addAttribute("marketCategory", marketCategory);
//    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
//    model.addAttribute("maxPage", 5);
//    return "marketing/marketingList";
//
//
//

    Pageable pageable = PageRequest.of(page.orElse(0), 6);

    Page<Marketing> marketings = marketingService.getMarketingPageByMarketCategory(marketingSearchDTO, marketCategory, pageable);

    List<String> emails = marketings.stream()
        .map(Marketing::getRegBy)
        .collect(Collectors.toList());

    List<Member> members = memberRepository.findByEmailIn(emails);

    Map<String, String> emailToNicknameMap = members.stream()
        .collect(Collectors.toMap(Member::getEmail, Member::getNickname));

    List<MarketingDTO> marketingDTOS = marketings.stream().map(marketing -> {
      MarketingDTO marketingDTO = new MarketingDTO();

      marketingDTO.setId(marketing.getId());
      marketingDTO.setMarketingTitle(marketing.getMarketingTitle());
      marketingDTO.setMarketingContent(marketing.getMarketingContent());
      marketingDTO.setMarketCategory(marketing.getMarketCategory());
      marketingDTO.setEventContent(marketing.getEventContent());
      marketingDTO.setEventContent(marketing.getEventContent());
      marketingDTO.setMarketOriImgName(marketing.getMarketOriImgName());
      marketingDTO.setMarketImgName(marketing.getMarketImgName());
      marketingDTO.setMarketImgUrl(marketing.getMarketImgUrl());
      marketingDTO.setMarketLink(marketing.getMarketLink());
      marketingDTO.setOwnerNickname(emailToNicknameMap.get(marketing.getRegBy()));
      return marketingDTO;
    }).collect(Collectors.toList());

    model.addAttribute("marketingDTOs", marketingDTOS);
    model.addAttribute("marketings", marketings);
    model.addAttribute("marketCategory", marketCategory);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);
    return "marketing/marketingList";

  }

  // 업장별 등록으로 보내기
  @GetMapping({"/{marketCategory}/new", "/{marketCategory}/new/{marketId}"})
  public String marketingForm(@PathVariable("marketCategory") MarketCategory marketCategory,
                              @PathVariable("marketId") Long marketingId,
                              Model model) {
    MarketingDTO marketingDTO;
    if(marketingId != null) {
      Marketing marketing = marketingRepository.findById(marketingId).orElseThrow(EntityNotFoundException::new);
      marketingDTO = MarketingDTO.of(marketing);
    } else {
      marketingDTO = new MarketingDTO();
    }

    marketingDTO.setMarketCategory(marketCategory);

    model.addAttribute("marketingDTO", marketingDTO);
    model.addAttribute("marketCategory", marketCategory);
    return "marketing/marketingForm";
  }

  // 업장별 등록
  @PostMapping({"/{marketCategory}/new"})
  public String marketingNew(@PathVariable("marketCategory") MarketCategory marketCategory,
                             @Valid MarketingDTO marketingDTO,
                             BindingResult bindingResult,
                             Principal principal,
                             @RequestParam("marketImgFile")MultipartFile marketImgFile,
                             Model model) throws IOException {
    if(bindingResult.hasErrors()){
      model.addAttribute("marketCategory", marketCategory);
      return "marketing/marketingForm";
    }
      // 사용자 정보 가져오기
      Member member = memberRepository.findByEmail(principal.getName());

      // 닉네임 설정
//      marketingDTO.setOwnerNickname(member.getNickname());

      marketingDTO.setMarketCategory(marketCategory);
      marketingDTO.setMember(member);

      // 이미지 파일 처리
      if(marketImgFile != null && !marketImgFile.isEmpty()){
        try{
          imgFileService.saveMarketImg(marketingDTO, marketImgFile);
        } catch (Exception e) {
          model.addAttribute("errorMessage", e.getMessage());
          return "marketing/marketingForm";
        }
      }

      if(StringUtils.equalsIgnoreCase(marketingDTO.getEventOrNot(), "end")){
        marketingDTO.setEventOrNot(EventOrNot.END);
        marketingDTO.setEventContent("행사 준비중");
      }

//      if(StringUtils.equalsIgnoreCase(marketingDTO.getEventOrNot(), "on")){
//        marketingDTO.setEventOrNot(EventOrNot.ON);
//        marketingDTO.setEventContent(marketingDTO.getEventContent());
//      }else{
//        marketingDTO.setEventOrNot(EventOrNot.END);
//        marketingDTO.setEventContent("행사 준비중");
//      }

      marketingService.saveMarketing(marketingDTO);
      return "redirect:/marketings/" + marketCategory + "/list";
  }

  // 상세조회는 없음 바로 링크타고 날아가면됨

  // 업장별 마케팅 수정
  @PostMapping({"/{marketCategory}/modify/{marketingId}"})
  public String marketingUpdate(@PathVariable("marketCategory") MarketCategory marketCategory,
                                @PathVariable("marketingId") Long marketingId,
                                @Valid MarketingDTO marketingDTO,
                                BindingResult bindingResult,
                                @RequestParam("marketImgFile") MultipartFile marketImgFile,
                                Model model) throws  IOException{
    if(bindingResult.hasErrors()){
      return "marketing/marketingForm";
    }

    // 이미지 처리
    if(marketImgFile != null && !marketImgFile.isEmpty()){
      try{
        MarketingDTO marketHaveImg = imgFileService.updateMarketImg(marketingId, marketImgFile);
        marketingDTO.setMarketOriImgName(marketHaveImg.getMarketOriImgName());
        marketingDTO.setMarketImgName(marketHaveImg.getMarketImgName());
        marketingDTO.setMarketImgUrl(marketHaveImg.getMarketImgUrl());
      } catch (Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "marketing/marketingForm";
      }
    }

    if(StringUtils.equalsIgnoreCase(marketingDTO.getEventOrNot(), "end")){
      marketingDTO.setEventOrNot(EventOrNot.END);
      marketingDTO.setEventContent("행사 준비중");
    }

//    if(StringUtils.equalsIgnoreCase(marketingDTO.getEventOrNot(), "on")){
//      marketingDTO.setEventOrNot(EventOrNot.ON);
//      marketingDTO.setEventContent(marketingDTO.getEventContent());
//    }else{
//      marketingDTO.setEventOrNot(EventOrNot.END);
//      marketingDTO.setEventContent(null);
//    }

    marketingService.updateMarketing(marketingDTO);
    return "redirect:/marketings/" + marketCategory + "/list";
  }

  // 마케팅 삭제
  @PostMapping({"/{marketCategory}/remove/{marketingId}"})
  public String marketingDelete(@PathVariable("marketCategory") MarketCategory marketCategory,
                                @PathVariable("marketingId") Long marketingId) throws Exception{
    imgFileService.deleteMarketImg(marketingId);
    marketingService.deleteMarketing(marketingId);
    return "redirect:/marketing/" + marketCategory + "/0";
  }

  // 행사중인거 조회
  @GetMapping({"/list/{eventOrNot}/{page}", "/list/{eventOrNot}"})
  public String getMarketingListByEvent(@PathVariable("eventOrNot") EventOrNot eventOrNot,
                                        @PathVariable("page") Optional<Integer> page,
                                        MarketingSearchDTO marketingSearchDTO,
                                        Model model){
    Pageable pageable = PageRequest.of(page.orElse(0), 5);
    Page<Marketing>  marketings = marketingService.getMarketingPageByEventOrNot(marketingSearchDTO, eventOrNot, pageable);

    // 닉네임 세팅
    List<String> emails = marketings.stream()
        .map(Marketing::getRegBy)
        .collect(Collectors.toList());

    List<Member> members = memberRepository.findByEmailIn(emails);

    Map<String, String> emailToNicknameMap = members.stream()
        .collect(Collectors.toMap(Member::getEmail, Member::getNickname));

    List<MarketingDTO> marketingDTOs = marketings.stream().map(marketing -> {
      MarketingDTO marketingDTO = new MarketingDTO();
      marketingDTO.setId(marketing.getId());
      marketingDTO.setMarketingTitle(marketing.getMarketingTitle());
      marketingDTO.setMarketingContent(marketing.getMarketingContent());
      marketingDTO.setRegTime(marketing.getRegTime());
      marketingDTO.setMarketLink(marketing.getMarketLink());
      marketingDTO.setMarketCategory(marketing.getMarketCategory());
      marketingDTO.setMarketOriImgName(marketing.getMarketOriImgName());
      marketingDTO.setMarketImgName(marketing.getMarketImgName());
      marketingDTO.setMarketImgUrl(marketing.getMarketImgUrl());

      if(StringUtils.equalsIgnoreCase(marketing.getEventOrNot(), "end")){
        marketingDTO.setEventOrNot(EventOrNot.END);
        marketingDTO.setEventContent("행사 준비중");
      }else{
        marketingDTO.setEventOrNot(marketing.getEventOrNot());
        marketingDTO.setEventContent(marketing.getEventContent());
      }

//      // 이벤트 활성화면
//      if(StringUtils.equalsIgnoreCase(marketing.getEventOrNot(), "on")){
//        marketingDTO.setEventOrNot(EventOrNot.ON);
//        marketingDTO.setEventContent(marketing.getEventContent());
//      }else{
//        marketingDTO.setEventOrNot(EventOrNot.END);
//        marketingDTO.setEventContent(marketing.getEventContent());  // 어차피 null임
//      }

      // 닉네임 설정
      marketingDTO.setOwnerNickname(emailToNicknameMap.get(marketing.getRegBy()));
      return marketingDTO;
    }).collect(Collectors.toList());

    model.addAttribute("marketingDTOs", marketingDTOs);
    model.addAttribute("marketings", marketings);
    model.addAttribute("eventOrNot", eventOrNot);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);
    return "marketing/marketingList";
  }

  // 수정 삭제는 마케팅 경로로 보내버림

}
