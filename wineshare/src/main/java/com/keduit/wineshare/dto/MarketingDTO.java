package com.keduit.wineshare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MarketingDTO {

  private Long id;

  @NotBlank(message = "업장명을 입력해주세요.")
  private String marketingTitle;

  @NotBlank(message = "업장 소개를 입력해주세요.")
  private String marketingContent;

  private String marketLink; // 업장 링크

  private String marketImgName;  // 업장별 사진파일명 uuid

  private String marketImgUrl; // 사진 경로

  private String marketOriImgName;  // 원본 이미지 이름

  private MarketCategory marketCategory;  // 업장분류

  private String ownerNickname; // 위에걸 써야하는지 이걸 써야하는지 둘다 있어야하는지 모르겠음

  // 여기까지가 업장등록 밑에가되면 프로모션
  private EventOrNot eventOrNot; // 행사상태

  private String eventContent;

  @JsonFormat(pattern = "yy-MM-dd")
  private LocalDateTime regTime;

  private Member member;

  public MarketingDTO() {}

  // 아마 안될거같은데 되면 땡큐고 일단 갈김
  private static ModelMapper modelMapper = new ModelMapper();

  public Marketing createMarketing(){return modelMapper.map(this, Marketing.class);}

  public static  MarketingDTO of(Marketing marketing){return modelMapper.map(marketing, MarketingDTO.class);}

  @QueryProjection
  public MarketingDTO(Long id, MarketCategory marketCategory, String marketingTitle, EventOrNot eventOrNot, String ownerNickname) {
    this.id = id;
    this.marketCategory = marketCategory;
    this.marketingTitle = marketingTitle;
    this.eventOrNot = eventOrNot;
    this.ownerNickname = ownerNickname;
  }

  @QueryProjection
  public MarketingDTO(Long id, MarketCategory marketCategory, String marketingTitle, EventOrNot eventOrNot, String ownerNickname, String marketImgUrl, String marketingContent, String marketLink) {
    this.id = id;
    this.marketCategory = marketCategory;
    this.marketingTitle = marketingTitle;
    this.eventOrNot = eventOrNot;
    this.ownerNickname = ownerNickname;
    this.marketingContent = marketingContent;
    this.marketImgUrl = marketImgUrl;
    this.marketLink = marketLink;
  }

}
