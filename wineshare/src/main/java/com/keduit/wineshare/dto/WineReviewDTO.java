package com.keduit.wineshare.dto;

import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WineReviewDTO {

  private Long id;

  @NotBlank(message = "리뷰를 입력해주세요.")
  private String regularReview;

  @NotBlank(message = "별점을 매겨주세요.")
  private double regularRating;

  private Member member;

  private Wine wine;

  private LocalDateTime regTime;

  private LocalDateTime updateTime;

}