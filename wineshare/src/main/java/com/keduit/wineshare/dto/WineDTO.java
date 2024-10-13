package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class WineDTO {

  private Long id;

  // null, 길이 0, empty 를 체크
  @NotBlank(message = "와인 이름을 입력하세요.")
  private String wineName;

  @NotBlank(message = "생산 국가를 입력하세요.")
  private String country;

  @NotBlank(message = "생산 지역을 입력하세요.")
  private String region;

  @Min(value = 0, message = "가격을 입력하세요.")
  private int price;

  @NotNull(message = "와인 종류를 선택하세요.")
  private WineType wineType;

  private String wineImg;

  private Long memberId;

  public WineDTO() {}

  @QueryProjection
  public WineDTO(Long id, String wineName, String country, String region, int price, WineType wineType, Long memberId,String wineImg) {
    this.id = id;
    this.wineName = wineName;
    this.country = country;
    this.region = region;
    this.price = price;
    this.wineType = wineType;
    this.wineImg = wineImg;
    this.memberId = memberId;
  }
}
