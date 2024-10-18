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

  private String searchType;
  private String searchQuery = "";

  private MarketCategory marketCategory;
  private EventOrNot eventOrNot;
  // 정렬옵션
  private String sortOrder;

}
