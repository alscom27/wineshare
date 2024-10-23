package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.thymeleaf.util.StringUtils;

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

  private String marketImgName;  // 업장별 사진파일명 uuid

  private String marketImgUrl; // 사진 경로

  private String marketOriImgName;  // 원본 이미지 이름

  @Enumerated(EnumType.STRING)
  private MarketCategory marketCategory;  // 업장분류

  @Enumerated(EnumType.STRING)
  private EventOrNot eventOrNot;  // 행사 상태

  private String eventContent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public void updateMarketing(MarketingDTO marketingDTO){
    this.marketingTitle = marketingDTO.getMarketingTitle();
    this.marketingContent = marketingDTO.getMarketingContent();
    this.marketLink = marketingDTO.getMarketLink();
    this.marketCategory = marketingDTO.getMarketCategory();

    if(this.marketImgName != null){
      this.marketImgName = marketingDTO.getMarketImgName();
      this.marketImgUrl = marketingDTO.getMarketImgUrl();
      this.marketOriImgName = marketingDTO.getMarketOriImgName();
    }

   this.eventOrNot = marketingDTO.getEventOrNot();
    this.eventContent = marketingDTO.getEventContent();
    System.out.println("=============6");
  }

}
