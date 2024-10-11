package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.service.MemberService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WineController {

  private final WineService wineService;
  private final MemberService memberService;

  @GetMapping("/list")
  public String wineList() {


    return "wine/wineList";
  }


  // /wines/new 로 들어가면 wineForm 페이지로 이동
  @GetMapping("/new")
  public String wineForm(Model model) {

    model.addAttribute("wineDTO", new WineDTO());

    return "wine/wineForm";
  }


  // 겟매핑을 통해 들어간 와인폼에서 입력 후 전송 시 포스트매핑으로 들어옴, 와인을 저장함
  @PostMapping("/new")
  public String saveWine(@Valid WineDTO wineDTO, BindingResult bindingResult, Model model, Principal principal) {
    Member member = memberService.findByEmail(principal.getName());
//    principal.getName();

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
    return "redirect:/wine/wineList"; // 루트, 홈페이지로 이동, 나중에 리스트로 이동하던가.. 바로 확인할 수 있는 곳으로 이동시켜야함
  }
}
