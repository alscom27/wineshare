package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.MemberPassModifyDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, MemberPassModifyDTO> {

  @Autowired
  private MemberService memberService;

  @Override
  public boolean isValid(MemberPassModifyDTO memberPassModifyDTO, ConstraintValidatorContext context) {

    String email = getCurrentMemberEmail();

    // 기존 비밀번호 확인
    if(!memberService.checkPassword(memberPassModifyDTO, email)){
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("비밀번호가 틀렸습니다.")
          .addPropertyNode("originPassword")
          .addConstraintViolation();
      return false;
    }

    // 새 비밀번호 검증
    if(!memberPassModifyDTO.getNewPassword().equals(memberPassModifyDTO.getConfirmPassword())){
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
          .addPropertyNode("newPassword")
          .addConstraintViolation();
      return false;
    }

    return true;
  }


  private String getCurrentMemberEmail() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(principal instanceof Member){
      return ((Member)principal).getEmail();
    }
    return null;
  }

}
