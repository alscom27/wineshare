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

  private String searchType;
  private String searchQuery = "";

  private MemberType memberType;
  private WithdrawStatus withdrawStatus;


}
