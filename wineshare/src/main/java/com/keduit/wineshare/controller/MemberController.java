package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.dto.MemberModifyDTO;
import com.keduit.wineshare.dto.MemberPassModifyDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @GetMapping("/agree")
  public String memberAgree(){
    return "member/memberAgree";
  }

  @GetMapping("/new")
  public String memberForm(Model model) {
//    HttpSession session = request.getSession();
    model.addAttribute("memberDTO", new MemberDTO());
    model.addAttribute("authority", "regular");
    return "member/memberForm";
  }

  @PostMapping("/new")
  public String memberForm(@Valid MemberDTO memberDTO,
                           BindingResult bindingResult,
                           Model model) {
    if (bindingResult.hasErrors()) {
      return "member/memberForm";
    }

    try {
      Member member = Member.createMember(memberDTO, passwordEncoder);
      memberService.saveMember(member);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }
    model.addAttribute("joinMessage", "회원가입을 환영합니다.");
    return "member/memberLoginForm";  // 로그인창으로
  }

  @GetMapping("/login")
  public String loginMember(HttpServletRequest request,
                            Model model) {

    // 세션에서 에러 메시지를 가져와서 모델에 추가
    String errorMsg = (String) request.getSession().getAttribute("loginErrorMsg");
    if (errorMsg != null) {
      model.addAttribute("loginErrorMsg", errorMsg);
      request.getSession().removeAttribute("loginErrorMsg"); // 메시지 제거
    }

    return "member/memberLoginForm";
  }



  @GetMapping({"/modify", "/modify/{memberId}"})
  public String modifyMember(Principal principal,
                             @PathVariable(value = "memberId", required = false) Long memberId,
                             Model model) {

    Member member;
    if(memberId != null) { // 멤버아이디를 통해 들어왔으면 멤버아이디로 멤버 정보 가져오기
      member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
    } else { // 멤버아이디가 없으면 프린시펄로 정보 가져오기
      member = memberRepository.findByEmail(principal.getName());
    }



    MemberModifyDTO memberModifyDTO = new MemberModifyDTO();
    memberModifyDTO.setId(member.getId());
    memberModifyDTO.setName(member.getName());
    memberModifyDTO.setNickname(member.getNickname());
    memberModifyDTO.setPhoneNumber(member.getPhoneNumber());


    String authority;

    if (StringUtils.equalsIgnoreCase(member.getMemberType(), "admin")) {
      authority = "admin";
    } else if (StringUtils.equalsIgnoreCase(member.getMemberType(), "expert")) {
      authority = "expert";
    } else {
      authority = "regular";
    }

    model.addAttribute("memberDTO", memberModifyDTO);
    model.addAttribute("authority", authority);

    return "member/memberForm";
  }

  @PostMapping("/modify")
  public String modifyMember(@Valid MemberModifyDTO memberModifyDTO,
                             BindingResult bindingResult,
                             Model model) {

    Member member = memberRepository.findById(memberModifyDTO.getId()).orElseThrow(EntityNotFoundException::new);

    String authority;

    if (StringUtils.equalsIgnoreCase(member.getMemberType(), "admin")) {
      authority = "admin";
    } else if (StringUtils.equalsIgnoreCase(member.getMemberType(), "expert")) {
      authority = "expert";
    } else {
      authority = "regular";
    }


    if (bindingResult.hasErrors()) {
      model.addAttribute("authority", authority);
      model.addAttribute("memberDTO", memberModifyDTO);
      return "redirect:/members/modify";
    }

    memberService.updateMember(memberModifyDTO);

    return "redirect:/";
  }

  @GetMapping("/pass/modify")
//  @ResponseBody
  public String memberPassModify(Principal principal,
                                 Model model) {

    Member member = memberRepository.findByEmail(principal.getName());

    Map<String, String> response = new HashMap<>();


    model.addAttribute("memberPassModifyDTO", new MemberPassModifyDTO());
    model.addAttribute("passType", "modify");
    return "member/memberPassForm";
  }

  @PostMapping("/pass/modify")
  public String memberPassModify(@Valid MemberPassModifyDTO memberPassModifyDTO,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 HttpServletRequest request,
                                 HttpServletResponse response, Model model) {


    System.out.println("controller================");
    System.out.println(memberPassModifyDTO.toString());

    // 비밀번호 변경
    memberService.changePassword(memberPassModifyDTO, principal.getName());

    // 비밀번호 변경 후 로그아웃 처리
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, response, (Authentication) principal);

    return "redirect:/members/login";

  }

  @GetMapping("/withdraw")
  public String withdrawMember(Principal principal,
                               Model model) {

    model.addAttribute("passType", "withdraw");
    return "member/memberPassForm";
  }

  @PostMapping("/withdraw")
  public String withdrawMember(Principal principal,
                               @RequestParam("withdrawPassword") String withdrawPassword,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
    Member member = memberRepository.findByEmail(principal.getName());

    boolean passMatches = passwordEncoder.matches(withdrawPassword, member.getPassword());

    if (passMatches) {
      member.setWithdrawStatus(WithdrawStatus.LEAVE);
      memberRepository.save(member);

      SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
      logoutHandler.logout(request, response, (Authentication) principal);

      model.addAttribute("message", "회원탈퇴가 완료되었습니다.");
      return "redirect:/";
    }

    model.addAttribute("passType", "withdraw");
    model.addAttribute("errorMsg", "비밀번호가 틀렸습니다.");
    return "member/memberPassForm";
  }

  // 등업 버튼
  @PostMapping("/expert/{boardStatus}/{memberId}")
  @ResponseBody
  public ResponseEntity<String> memberUpgrade(@PathVariable("boardStatus") BoardStatus boardStatusStr,
                                              @PathVariable("memberId") Long memberId,
                                              Model model) {
    try {
      Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
      member.setMemberType(MemberType.EXPERT);
      memberService.upgradeMember(member);
      return ResponseEntity.ok("회원 권한이 변경되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("등업 처리중 오류가 발생했습니다." + e.getMessage());
    }
  }


}
