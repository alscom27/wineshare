package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class MemberModifyDTO {

  private Long id;

  @NotBlank(message = "이름은 필수 입력입니다.")
  private String name;

  @NotBlank(message = "닉네임은 필수 입력입니다.")
  private String nickname;

  @NotBlank(message = "전화번호는 필수 입력입니다.")
  private String phoneNumber;

}
