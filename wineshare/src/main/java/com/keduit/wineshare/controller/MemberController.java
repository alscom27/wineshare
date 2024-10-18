package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Log4j2
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
  public String loginMember(){
    return "member/memberLoginForm";
  }

 @GetMapping("/login/error")
  public String loginError(Model model){
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
    return "member/memberLoginForm";
  }

  @GetMapping("/modify")
  public String modifyMember(Principal principal,
                             Model model){

    Member member = memberRepository.findByEmail(principal.getName());

    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberDTO.getId());
    memberDTO.setEmail(member.getEmail());
    memberDTO.setName(member.getName());
    memberDTO.setNickname(member.getNickname());
    memberDTO.setPhoneNumber(member.getPhoneNumber());
//    memberDTO.setPassword();

    model.addAttribute("memberDTO", memberDTO);

    return "member/memberForm";
  }


  // 임시 컨트롤러
  @GetMapping("/memberAgree")
  public String agree(){
    log.info("memberAgree");
    return "member/memberAgree";
  }


}
