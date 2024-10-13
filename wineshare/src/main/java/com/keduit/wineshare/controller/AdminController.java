package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.dto.*;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.service.BoardService;
import com.keduit.wineshare.service.MarketingService;
import com.keduit.wineshare.service.MemberService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

  //주입
  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;
  private final WineService wineService;
  private final BoardService boardService;
  private final MarketingService marketingService;


  // 관리자 회원가입
  // adminId = 대표 아이디
  @GetMapping("/{adminId}/new")
  public String adminForm(@PathVariable("adminId") String adminId,
                          Model model) {
    if(!adminId.equals("wineshare8022")){
      // 회원 데이타 리스트
      // 여기서 관리자 회원가입 버튼 있으면좋겠음
      return "redirect:/admins/memberList";
    }

    model.addAttribute("memberDTO", new MemberDTO());
    return "/member/memberForm";  // 일반회원가입과 동일한폼으로 보냄 명시적으로 체크 버튼있어도좋을듯 관리자 체크버튼
  }

  @PostMapping("/{adminId}/new")
  public String adminForm(@PathVariable("adminId") String adminId,
                          @Valid MemberDTO memberDTO,
                          BindingResult bindingResult,
                          Model model) {
    if(bindingResult.hasErrors()){
      return "member/memberForm";
    }

    if(!adminId.equals("wineshare8022")){
      return "redirect:/member/memberForm";
    }
    try {
      memberDTO.setMemberType(MemberType.ADMIN);  // 체크해줄지 자동으로해줄지는 말해보자
      Member member = Member.createMember(memberDTO, passwordEncoder);
      memberService.saveMember(member);
    }catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }
    // 메인으로 보내줄까? 관리자 대시보드로 보여줄까?
    return "redirect:/";

  }

  // 회원 리스트
  @GetMapping({"/members/list", "/members/list{page}"})
  public String adminMemberList(@PathVariable("page") Optional<Integer> page,
                           MemberSearchDTO memberSearchDTO,
                           Model model){
    // 회원 타입별, 회원탈퇴여부별로 필터링 추가하기

    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<Member> members = memberService.getMemberPage(memberSearchDTO, pageable);
    model.addAttribute("members", members);
    model.addAttribute("memberSearchDTO", memberSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }

  //와인 데이터 리스트
  @GetMapping({"wines/list", "wines/list/{page}"})
  public String adminWineList(WineSearchDTO wineSearchDTO,
                              @PathVariable("page")Optional<Integer> page,
                              Model model) {
    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);
    model.addAttribute("wines", wines);
    model.addAttribute("wineSearchDTO", wineSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }
  // 따로 관리자 대시보드에선 리스트로 그냥 횡스크롤 길게보여주고 각각의 데이터들에 대한 수정 삭제버튼만 있음(누르면 각각의 수정 삭제 폼으로 아이디 들고 튀고) 될거같은데? 어찌생각하시나요?

  // 아로마휠 리스트

  // 푸트페어링 리스트

  // 게시판 리스트
  @GetMapping({"boards/list", "boards/list{page}"})
  public String adminBoardList(@PathVariable("page") Optional<Integer> page,
                               BoardSearchDTO boardSearchDTO,
                               Model model) {
    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<Board> boards = boardService.getBoardPage(boardSearchDTO, pageable);

    model.addAttribute("boards", boards);
    model.addAttribute("boardSearchDTO", boardSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }

  // 마케팅 리스트
  @GetMapping({"marketing/list", "marketing/list/{page}"})
  public String adminMarketingList(@PathVariable("page") Optional<Integer> page,
                                   MarketingSearchDTO marketingSearchDTO,
                                   Model model){
    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<Marketing> marketings = marketingService.getMarketingPage(marketingSearchDTO, pageable);

    model.addAttribute("marketings", marketings);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }


}
