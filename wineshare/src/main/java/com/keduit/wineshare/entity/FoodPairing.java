package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

// 은우햄이 해주심
@Entity
@Getter
@Setter
@ToString
public class FoodPairing extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "food_pairing_id")
  private Long id;

  private String foodPairingName;
}
