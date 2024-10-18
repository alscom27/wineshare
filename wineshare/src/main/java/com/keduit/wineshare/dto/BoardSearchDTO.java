package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.BoardStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearchDTO {

  private String searchType;
  private String searchQuery = "";
  private BoardStatus searchBoardStatus;

}
