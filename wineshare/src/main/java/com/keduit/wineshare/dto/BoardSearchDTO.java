package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.BoardStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearchDTO {

  // T 제목 C 내용 W 작성자
  private String searchType;
  private String searchQuery = "";

  // NOTICE, UPGRADE, QUESTION, REQUEST
  private BoardStatus searchBoardStatus;


}
