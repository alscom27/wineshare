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

  // 현재 이벤트상태를 알려주는게 두개가 되어버림
  //체크이벤트와 이벤트 올낫 (동적쿼리를 이벤트올낫으로 만들어서 체크이벤트 빼는게낳을듯)
  @Column(nullable = false)
  private int checkEvent; //체크가 혹시 잘못되더라도 디폴트 0으로 우선 선언

  private String checkContent;  // 체크가 1되면 폼 활성화


  @Enumerated
  private MarketCategory marketCategory;  // 업장분류

  @Enumerated
  private EventOrNot eventOrNot;  // 행사 상태

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member member;

  public void updateMarketing(MarketingDTO marketingDTO){
    this.marketingTitle = marketingDTO.getMarketingTitle();
    this.marketingContent = marketingDTO.getMarketingContent();
    this.marketLink = marketingDTO.getMarketLink();
    this.marketImg = marketingDTO.getMarketImg();
    this.checkEvent = marketingDTO.getCheckEvent();
    this.checkContent = marketingDTO.getCheckContent();
    this.marketCategory = marketingDTO.getMarketCategory();

  }


}
