package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class WineReview extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wine_review_id")
  private Long id;

  // 전문가 리뷰, 별점은 정보 입력 폼에서 받음
  private String regular_review;

  private String regular_rating;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wine_id")
  private Wine wine;

}
