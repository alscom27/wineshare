package com.keduit.wineshare.dto;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

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

  private String marketImg; // 저장된 업장사진 경로?

  private MultipartFile marketImgFile; // 업로드할 이미지 파일?

  private MarketCategory marketCategory;  // 업장분류

  private EventOrNot eventOrNot; // 행사상태

  private Member member;

  private String ownerNickname; // 위에걸 써야하는지 이걸 써야하는지 둘다 있어야하는지 모르겠음

  // 여기까지가 업장등록 밑에가되면 프로모션

  private int checkEvent; //0이면 업장등록이고 1이면  // selected는 0으로(디폴트0)

  private String checkContent;  //체크가 1이면 활성화 (행사내용)

  // 아마 안될거같은데 되면 땡큐고 일단 갈김
  private static ModelMapper modelMapper = new ModelMapper();

  public Marketing createMarketing(){return modelMapper.map(this, Marketing.class);}

  public static  MarketingDTO of(Marketing marketing){return modelMapper.map(marketing, MarketingDTO.class);}

}
