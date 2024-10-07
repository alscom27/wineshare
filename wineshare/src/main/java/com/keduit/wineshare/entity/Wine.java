package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.WineType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Wine extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wine_id")
  private Long id;

  private String wineName;

  @Enumerated
  private WineType wineType;

  private String country;

  private String region;

  private int price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member user;
}
