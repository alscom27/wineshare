package com.keduit.wineshare.dto;

import com.keduit.wineshare.repository.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ValidPassword
@Getter
@Setter
@ToString
public class MemberPassModifyDTO {

//  private Long id;

  private String originPassword;  // 기존 비밀번호

  private String newPassword; // 새로운 비밀번호

  private String confirmPassword; // 새로운 비밀번호 확인

}
