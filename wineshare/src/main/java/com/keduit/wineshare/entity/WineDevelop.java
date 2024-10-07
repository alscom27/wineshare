package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
  private double sweet;

  // acidic, soft
  private double acidity;

  // bold, light
  private double body;

  // tannic, smooth
  private double tannin;

  // fizzy, gentle
  private double fizz;

  // 이거는 보류 우선
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "food_pairing_id")
  private List<FoodPairing> foodPairingList = new ArrayList<>();

  // 여기도 보류
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "aroma_wheel_id")
  private List<AromaWheel> aromaWheelList = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wine_id")
  private Wine wine;

}
