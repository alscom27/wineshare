package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class WineDevelopDTO {

  private Long windId;

  @Min(0)
  @Max(5)
  private double expertRating;

  @NotBlank(message = "리뷰 코멘트는 필수입니다.")
  private String expertComment;

  // 높으면 sweet, 낮으면 dry
  @Min(0)
  @Max(5)
  private double sweetness;

  // acidic, soft
  @Min(0)
  @Max(5)
  private double acidity;

  // bold, light
  @Min(0)
  @Max(5)
  private double body;

  // tannic, smooth
  @Min(0)
  @Max(5)
  private double tannin;

  // fizzy, gentle
  @Min(0)
  @Max(5)
  private double fizz;

  private String aromaOne;
  private String aromaTwo;
  private String foodOne;
  private String foodTwo;

}
