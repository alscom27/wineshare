package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PriceRangeDTO {

  private Double minPrice;
  private Double maxPrice;

  public PriceRangeDTO(Double minPrice, Double maxPrice) {
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
  }

}

