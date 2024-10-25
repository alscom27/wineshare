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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
@Log4j2
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



  // 데이터 관리 선택페이지
  @GetMapping("/dataChoiList")
  public String data(Model model){
    log.info("dataChoiList");

    Pageable pageable = PageRequest.of(0, 6);
    BoardSearchDTO boardSearchDTO = new BoardSearchDTO();
    MarketingSearchDTO marketingSearchDTO = new MarketingSearchDTO();
    WineSearchDTO wineSearchDTO = new WineSearchDTO();
    MemberSearchDTO memberSearchDTO = new MemberSearchDTO();
    Page<BoardDTO> boards = boardService.getBoardPage(boardSearchDTO, pageable);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);
    Page<MarketingDTO> marketings = marketingService.getMarketingPage(marketingSearchDTO, pageable);
    Page<MemberDTO> members = memberService.getMemberPage(memberSearchDTO, pageable);

    model.addAttribute("boards", boards.getContent());
    model.addAttribute("wines", wines.getContent());
    model.addAttribute("marketings", marketings.getContent());
    model.addAttribute("members", members.getContent());
    return "admin/dataChoiList";
  }


  // 와인 리스트
  @GetMapping({"/wines/list"})
  public String adminWineList(Model model) {
    model.addAttribute("categoryWines", "wines");
    return "admin/dataList";
  }
  // 게시판 리스트
  @GetMapping({"/boards/list"})
  public String adminBoardList(Model model) {
    model.addAttribute("categoryBoards", "boards");
    return "admin/dataList";
  }
  // 마케팅 리스트
  @GetMapping({"/marketings/list"})
  public String adminMarketingList(Model model) {
    model.addAttribute("categoryMarketings", "marketings");
    return "admin/dataList";
  }
  // 회원 리스트
  @GetMapping({"/members/list"})
  public String adminMemberList(Model model) {
    model.addAttribute("categoryMembers", "members");
    return "admin/dataList";
  }

  // 게시판 리스트 (ajax)
  @GetMapping({"/ajax/boards/list", "/ajax/boards/list/{page}"})
  @ResponseBody
  public ResponseEntity<Map<String, Object>> adminBoardList(@PathVariable("page") Optional<Integer> page,
                                                           BoardSearchDTO boardSearchDTO
                                                       ) {
    Pageable pageable = PageRequest.of(page.orElse(0), 20);
    Page<BoardDTO> boards = boardService.getBoardPage(boardSearchDTO, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("boards", boards);
    response.put("currentPage", boards.getNumber());
    response.put("totalPages", boards.getTotalPages());

    return ResponseEntity.ok(response);
  }

  // 와인 리스트 (ajax)
  @GetMapping({"/ajax/wines/list", "/ajax/wines/list/{page}"})
  @ResponseBody // JSON을 반환하기 위해 추가
  public ResponseEntity<Map<String, Object>> wineListJson(WineSearchDTO wineSearchDTO,
                                                          @PathVariable("page") Optional<Integer> page) {
    Pageable pageable = PageRequest.of(page.orElse(0), 20);
    Page<WineDTO> wines = wineService.getWinePage(wineSearchDTO, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("wines", wines);
    response.put("totalPages", wines.getTotalPages());
    response.put("currentPage", wines.getNumber());


    return ResponseEntity.ok(response);
  }

  // 마케팅 리스트 (ajax)
  @GetMapping({"/ajax/marketings/list", "/ajax/marketings/list/{page}"})
  @ResponseBody
  public ResponseEntity<Map<String, Object>> adminMarketingList(@PathVariable("page") Optional<Integer> page,
                                   MarketingSearchDTO marketingSearchDTO){

    Pageable pageable = PageRequest.of(page.orElse(0), 20);
    Page<MarketingDTO> marketings = marketingService.getMarketingPage(marketingSearchDTO, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("marketings", marketings);
    response.put("currentPage", marketings.getNumber());
    response.put("totalPages", marketings.getTotalPages());

    return ResponseEntity.ok(response);
  }

  // 회원 리스트(ajax)
  @GetMapping({"/ajax/members/list", "/ajax/members/list/{page}"})
  @ResponseBody
  public ResponseEntity<Map<String, Object>> adminMemberList(@PathVariable("page") Optional<Integer> page,
                                                             MemberSearchDTO memberSearchDTO){
    Pageable pageable = PageRequest.of(page.orElse(0), 20);
    Page<MemberDTO> members = memberService.getMemberPage(memberSearchDTO, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("members", members);
    response.put("currentPage", members.getNumber());
    response.put("totalPages", members.getTotalPages());

    return ResponseEntity.ok(response);
  }








}
