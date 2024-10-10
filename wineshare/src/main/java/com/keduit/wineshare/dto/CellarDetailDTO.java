package com.keduit.wineshare.dto;

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

  public CellarDetailDTO(Long cellarWineId, String wineName, String wineImg) {
    this.cellarWineId = cellarWineId;
    this.wineName = wineName;
    this.wineImg = wineImg;
  }
}
