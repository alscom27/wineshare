package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.WineType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WineSearchDTO {


  // 와인 타입으로 필터링
  private WineType searchWineType;

  // 정렬 ( 가격높은순, 가격낮은순, 별점높은순, 별점낮은순 )
  private String sortBy;

  // 가격 범위로 필터링
  private Integer minPrice;
  private Integer maxPrice;

}

