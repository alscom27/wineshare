package com.keduit.wineshare.dto;

import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.querydsl.core.annotations.QueryProjection;
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
  private Long id;

  private Long wineId;

  private Long memberId;

  @Min(1)
  @Max(5)
  private double expertRating;

  @NotBlank(message = "리뷰 코멘트는 필수입니다.")
  private String expertComment;

  // 높으면 sweet, 낮으면 dry
  private double sweetness;
  // acidic, soft
  private double acidity;
  // bold, light
  private double body;
  // tannic, smooth
  private double tannin;
  // fizzy, gentle
  private double fizz;

  private String aromaOne;
  private String aromaTwo;
  private String foodOne;
  private String foodTwo;

  public WineDevelopDTO() {}

  @QueryProjection
  public WineDevelopDTO(Long id, Long wineId, Long memberId, double expertRating, String expertComment, double sweetness, double acidity, double body, double tannin, double fizz, String aromaOne, String aromaTwo, String foodOne, String foodTwo) {
    this.id = id;
    this.wineId = wineId;
    this.memberId = memberId;
    this.expertRating = expertRating;
    this.expertComment = expertComment;
    this.sweetness = sweetness;
    this.acidity = acidity;
    this.body = body;
    this.tannin = tannin;
    this.fizz = fizz;
    this.aromaOne = aromaOne;
    this.aromaTwo = aromaTwo;
    this.foodOne = foodOne;
    this.foodTwo = foodTwo;
  }
}
