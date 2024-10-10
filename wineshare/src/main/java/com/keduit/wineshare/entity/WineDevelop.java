package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;


@Entity
@Getter
@Setter
@ToString
public class WineDevelop extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wine_develop_id")
  private Long id;

  private double expertRating;

  private String expertComment;

  // 높으면 sweet, 낮으면 drt
  private double sweetness;

  // acidic, soft
  private double acidity;

  // bold, light
  private double body;

  // tannic, smooth
  private double tannin;

  // fizzy, gentle
  private double fizz;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wine_id")
  private Wine wine;

  private String aromaOne;
  private String aromaTwo;
  private String foodOne;
  private String foodTwo;

  // 기본 생성자
  public WineDevelop() {}

  // 모든 필드를 포함하는 생성자
  public WineDevelop(double expertRating, String expertComment, double sweetness,
                     double acidity, double body, double tannin, double fizz,
                     String aromaOne, String aromaTwo, String foodOne, String foodTwo,
                     Member user, Wine wine) {
    this.expertRating = expertRating;
    this.expertComment = expertComment;
    this.sweetness = sweetness;
    this.acidity = acidity;
    this.body = body;
    this.tannin = tannin;
    this.fizz = fizz;
    this.user = user;
    this.wine = wine;
    this.aromaOne = aromaOne;
    this.aromaTwo = aromaTwo;
    this.foodOne = foodOne;
    this.foodTwo = foodTwo;
  }

}
