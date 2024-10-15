package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.WineType;
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

  public CellarDetailDTO(Long cellarWineId, String wineName, String wineImg, WineType wineType) {
    this.cellarWineId = cellarWineId;
    this.wineName = wineName;
    this.wineImg = wineImg;
    this.wineType = wineType;
  }
}
