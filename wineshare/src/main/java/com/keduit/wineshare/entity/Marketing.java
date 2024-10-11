package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

// 그냥 업장별로 보여주는곳(행사중이던 아니던 업장별분류)
// 행사중인건(업장별 안나누고 모아서 행사중인거전부)
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

  private String marketLink; // 업장별 링크

  private String marketImg;  // 업장별 사진



  private String checkContent;  // 행사 설명, 체크가 1되면 폼 활성화


  @Enumerated
  private MarketCategory marketCategory;  // 업장분류

  @Enumerated
  private EventOrNot eventOrNot;  // 행사 상태

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public void updateMarketing(MarketingDTO marketingDTO){
    this.marketingTitle = marketingDTO.getMarketingTitle();
    this.marketingContent = marketingDTO.getMarketingContent();
    this.marketLink = marketingDTO.getMarketLink();
    this.marketImg = marketingDTO.getMarketImg();
    this.eventOrNot = marketingDTO.getEventOrNot();
    this.checkContent = marketingDTO.getCheckContent();
    this.marketCategory = marketingDTO.getMarketCategory();

  }


}
