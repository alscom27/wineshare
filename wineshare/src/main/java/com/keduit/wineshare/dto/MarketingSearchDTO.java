package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketingSearchDTO {

  // T 매자이름, N 등록자닉네임
  private String searchType;
  private String searchQuery = "";

  // WINEBAR, BOTTLESHOP, BISTRO
  private MarketCategory marketCategory;
  // END, EVENT, PROMOTION
  private EventOrNot eventOrNot;

  private String sortOrder;

}
