package com.keduit.wineshare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @JoinColumn(name = "member_id")
  private Member member;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wine_id")
  private Wine wine;

  private String aromaOne;
  private String aromaTwo;
  private String foodOne;
  private String foodTwo;



}
