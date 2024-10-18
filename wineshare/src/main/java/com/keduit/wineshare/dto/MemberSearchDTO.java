package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSearchDTO {


  // N 이름, E 이메일, K 닉네임
  private String searchType;

  private String searchQuery = "";

  // REGULAR, EXPERT, ADMIN
  private MemberType memberType;
  // STAY, LEAVE
  private WithdrawStatus withdrawStatus;


}
