package com.keduit.wineshare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

//은우햄이 해주심
@Entity
@Getter
@Setter
@ToString
public class AromaWheel extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "aroma_wheel_id")
  private Long id;

  private String aroma;

  private String aromaValue;

  private String aromaImg;




}
