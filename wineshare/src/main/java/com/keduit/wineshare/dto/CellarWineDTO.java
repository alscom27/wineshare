package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CellarWineDTO {

  @NotNull(message = "와인을 선택해주세요.")
  private Long wineId;
}
