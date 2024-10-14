package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.*;
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
    model.addAttribute("authority", "admin");
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
    return "member/memberLoginForm";

  }

  // 회원 리스트
  @GetMapping({"/members/list", "/members/list{page}"})
  public String adminMemberList(@PathVariable("page") Optional<Integer> page,
                           MemberSearchDTO memberSearchDTO,
                           MemberType memberType,
                           WithdrawStatus withdrawStatus,
                           Model model){
    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<Member> members ;

    // 회원 타입별, 회원탈퇴여부별로 필터링 추가하기
    if(memberType==null && withdrawStatus==null){
      // 둘다 기본이면 전체 조회
      members = memberService.getMemberPage(memberSearchDTO, pageable);
    }else if(memberType==null && withdrawStatus!=null){
      // 회원 탈퇴여부로 조회
      members = memberService.getMemberPageByWithdrawStatus(memberSearchDTO, withdrawStatus, pageable);
    }else {
      // 회원 타입별 조회 둘다는 뺌 해보고 추가가 차라리 더 편하면 추가 가능
      members = memberService.getMemberPageByMemberType(memberSearchDTO, memberType, pageable);
    }


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
                               BoardStatus boardStatus,
                               Model model) {

    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    // 정렬타입(보드상태)이 기본(null)이면 전체조회
    if(boardStatus==null){
      Page<Board> boards = boardService.getBoardPage(boardSearchDTO, pageable);
      model.addAttribute("boards", boards);
      model.addAttribute("boardSearchDTO", boardSearchDTO);
      model.addAttribute("maxPage", 5);

      return "admin/dataList";
    }

    // 아니면 상태별조회
    Page<Board> boards = boardService.getBoardPageByStatus(boardSearchDTO, boardStatus, pageable);

    model.addAttribute("boards", boards);
    model.addAttribute("boardSearchDTO", boardSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }

  // 마케팅 리스트
  @GetMapping({"marketing/list", "marketing/list/{page}"})
  public String adminMarketingList(@PathVariable("page") Optional<Integer> page,
                                   MarketingSearchDTO marketingSearchDTO,
                                   MarketCategory marketCategory,
                                   EventOrNot eventOrNot,
                                   Model model){

    Pageable pageable = PageRequest.of(page.orElse(0), 10);
    Page<Marketing> marketings;
    if(marketCategory==null && eventOrNot==null){ //업장분류, 행사여부 비어있으면 전체 조회
      marketings = marketingService.getMarketingPage(marketingSearchDTO, pageable);
    }else if(eventOrNot != null){
      marketings = (marketCategory == null) //업장분류 비고, 행사여부 있으면 행사여부로 필터링
          ? marketingService.getMarketingPageByEventOrNot(marketingSearchDTO, eventOrNot, pageable)
          // 둘 다 있으면 둘 다 필터링 조회
          : marketingService.getMarketingPageByEventAndCategory(marketingSearchDTO, marketCategory, eventOrNot, pageable);

    }else { //업장부류 있고 행사여부 비면 업장분류별 조회
      marketings = marketingService.getMarketingPageByMarketCategory(marketingSearchDTO, marketCategory, pageable);
    }

    model.addAttribute("marketings", marketings);
    model.addAttribute("marketingSearchDTO", marketingSearchDTO);
    model.addAttribute("maxPage", 5);

    return "admin/dataList";
  }


}
