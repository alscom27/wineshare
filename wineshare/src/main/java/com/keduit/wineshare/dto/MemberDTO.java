package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class MemberDTO {
  // 정책 맞추고 추가해서 손봐야할듯
  @NotBlank(message = "이메일은 필수 입력입니다.")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @NotBlank(message = "이름은 필수 입력입니다.")
  private String name;

  @NotBlank(message = "닉네임은 필수 입력입니다.")
  private String nickname;

  @NotBlank(message = "비밀번호는 필수 입력입니다.")
  @Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
  private String password;

  @NotBlank(message = "전화번호는 필수 입력입니다.")
  @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "핸드폰 번호는 010-XXXX-XXXX 형식으로 입력해야 합니다.")
  private String phoneNumber;

  @NotBlank(message = "주민등록번호는 필수 입력입니다.")
  @Pattern(regexp = "^(\\d{6})-(\\d{7})$", message = "주민등록번호는 123456-1234567 형식으로 입력해야 합니다.")
  // 이건 사용자편의상 스크립트로 하이픈을 붙여주던 하이픈 앞뒤로 입력받고 하이픈 붙여서 넘기는게 좋아보임
  private String RRN;

}