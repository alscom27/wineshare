package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.dto.MemberModifyDTO;
import com.keduit.wineshare.dto.MemberPassModifyDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.MemberService;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/new")
  public String memberForm(Model model){
//    HttpSession session = request.getSession();
    model.addAttribute("memberDTO", new MemberDTO());
    model.addAttribute("authority", "regular");
    return "member/memberForm";
  }

  @PostMapping("/new")
  public String memberForm(@Valid MemberDTO memberDTO,
                           BindingResult bindingResult,
                           Model model){
    if(bindingResult.hasErrors()){
      return "member/memberForm";
    }

    try{
      Member member = Member.createMember(memberDTO, passwordEncoder);
      memberService.saveMember(member);
    }catch(IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }
    return "redirect:/";  // 메인

  }

 @GetMapping("/login")
  public String loginMember(HttpServletRequest request,
                            Model model){

   // 세션에서 에러 메시지를 가져와서 모델에 추가
   String errorMsg = (String) request.getSession().getAttribute("loginErrorMsg");
   if (errorMsg != null) {
     model.addAttribute("loginErrorMsg", errorMsg);
     request.getSession().removeAttribute("loginErrorMsg"); // 메시지 제거
   }

    return "member/memberLoginForm";
  }

  // @GetMapping("/login/error")
  //  public String loginError(Model model){
  //    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
  //    return "member/memberLoginForm";
  //  }

  @GetMapping("/modify")
  public String modifyMember(Principal principal,
                             Model model){

    Member member = memberRepository.findByEmail(principal.getName());

//    MemberDTO memberDTO = new MemberDTO();
//    memberDTO.setId(member.getId());
//    memberDTO.setEmail(member.getEmail());
//    memberDTO.setName(member.getName());
//    memberDTO.setNickname(member.getNickname());
//    memberDTO.setPhoneNumber(member.getPhoneNumber());
//    memberDTO.setPassword(member.getPassword());

    MemberModifyDTO memberModifyDTO = new MemberModifyDTO();
    memberModifyDTO.setId(member.getId());
    memberModifyDTO.setName(member.getName());
    memberModifyDTO.setNickname(member.getNickname());
    memberModifyDTO.setPhoneNumber(member.getPhoneNumber());

    String authority;

    if(StringUtils.equalsIgnoreCase(member.getMemberType(), "admin")){
      authority = "admin";
    }else if(StringUtils.equalsIgnoreCase(member.getMemberType(), "expert")){
      authority = "expert";
    }else {
      authority = "regular";
    }

    model.addAttribute("memberDTO", memberModifyDTO);
    model.addAttribute("authority", authority);

    return "member/memberForm";
  }

  @PostMapping("/modify")
  public String modifyMember(@Valid MemberModifyDTO memberModifyDTO,
                             BindingResult bindingResult,
                             Model model){

    Member member = memberRepository.findById(memberModifyDTO.getId()).orElseThrow(EntityNotFoundException::new);

    String authority;

    if(StringUtils.equalsIgnoreCase(member.getMemberType(), "admin")){
      authority = "admin";
    }else if(StringUtils.equalsIgnoreCase(member.getMemberType(), "expert")){
      authority = "expert";
    }else {
      authority = "regular";
    }

//    System.out.println("===============");
//    System.out.println(memberDTO.toString());
//    System.out.println(member.toString());

    if(bindingResult.hasErrors()){
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
                                 Model model){

    Member member = memberRepository.findByEmail(principal.getName());

    Map<String, String> response = new HashMap<>();

//    if(member.getId() != memberId){
//      response.put("errorMessage", "너 누구야");
//      return response; // 에러 메시지를 JSON 형태로 반환
//    }

//    response.put("redirectUrl", "/member/memberPassChangeForm"); // 비밀번호 변경 폼의 URL
//    return response; // 성공 시 리다이렉션 URL을 반환

    model.addAttribute("memberPassModifyDTO", new MemberPassModifyDTO());
    model.addAttribute("passType", "modify");
    return "member/memberPassForm";
  }

  @PostMapping("/pass/modify")
  public String memberPassModify(@Valid MemberPassModifyDTO memberPassModifyDTO,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

    // 어의없네 이거막아서 된다고?
//    if(bindingResult.hasErrors()){
//      System.out.println(bindingResult.getAllErrors());
//      System.out.println("controller has error================");
//      System.out.println(memberPassModifyDTO.toString());
//      return "member/memberPassChangeForm";
//    }

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
                               Model model){

    model.addAttribute("passType", "withdraw");
    return "member/memberPassForm";
  }

  @PostMapping("/withdraw")
  public String withdrawMember(Principal principal,
                               @RequestParam("withdrawPassword") String withdrawPassword,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model){
    Member member = memberRepository.findByEmail(principal.getName());

    boolean passMatches = passwordEncoder.matches(withdrawPassword, member.getPassword());

    if(passMatches){
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



}
