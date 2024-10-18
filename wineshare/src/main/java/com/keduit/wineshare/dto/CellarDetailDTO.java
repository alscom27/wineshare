package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.WineType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CellarDetailDTO {

  private Long cellarWineId;

  private String wineName;

  private String wineImg;

  private WineType wineType;

  private Long wineId;

  @QueryProjection
  public CellarDetailDTO(Long cellarWineId, String wineName, String wineImg, WineType wineType, Long wineId) {
    this.cellarWineId = cellarWineId;
    this.wineName = wineName;
    this.wineImg = wineImg;
    this.wineType = wineType;
    this.wineId = wineId;
  }
}
