package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketingSearchDTO {

  private String searchType;
  private String searchQuery = "";

  // 정렬옵션
  private String sortOrder;

}
