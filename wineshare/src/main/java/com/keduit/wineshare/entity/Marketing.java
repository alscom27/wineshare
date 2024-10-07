package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.MarketCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Marketing extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "marketing_id")
  private Long id;

  private String  marketingTitle;

  private String marketingContent;

  @Column(nullable = false)
  private int checkEvent = 0; //체크가 혹시 잘못되더라도 디폴트 0으로 우선 선언

  private String checkContent;  // 체크가 1되면 폼 활성화

  private String marketLink; // 업장별 링크

  private String marketImg;  // 업장별 사진

  @Enumerated
  private MarketCategory marketCategory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member user;




}
