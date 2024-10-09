package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.WineType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class WineDTO {

  // null, 길이 0, empty 를 체크
  @NotBlank(message = "와인 이름을 입력하세요.")
  private String wineName;

  // null, 길이 0 체크
  @NotEmpty(message = "생산 국가를 입력하세요.")
  private String country;

  @NotEmpty(message = "생산 지역을 입력하세요.")
  private String region;

  @NotEmpty(message = "가격을 입력하세요.")
  private int price;

  private WineType wineType;

  private String wineImg;
}
