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
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WineReviewDTO {

  private Long id;

  @NotBlank(message = "리뷰를 입력해주세요.")
  private String regularReview;

  @Min(1)
  @Max(5)
  @NotBlank(message = "별점을 매겨주세요.")
  private double regularRating;

  private Member member;

  private Wine wine;

  private Long wineId;

  private Long memberId;

  private LocalDateTime regTime;

  private LocalDateTime updateTime;

  private String memberNickname;

  public WineReviewDTO() {}

  @QueryProjection
  public WineReviewDTO(Long id, Long wineId, Long memberId, String memberNickname, double regularRating, String regularReview) {
    this.id = id;
    this.wineId = wineId;
    this.memberId = memberId;
    this.memberNickname = memberNickname;
    this.regularRating = regularRating;
    this.regularReview = regularReview;
  }
}
