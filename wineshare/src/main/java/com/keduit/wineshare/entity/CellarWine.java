package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class CellarWine extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cellar_wine_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cellar_id")
  private Cellar cellar;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wine_id")
  private Wine wine;

  public static CellarWine createCellarWine(Cellar cellar, Wine wine) {
    CellarWine cellarWine = new CellarWine();
    cellarWine.setCellar(cellar);
    cellarWine.setWine(wine);
    return cellarWine;
  }




}
