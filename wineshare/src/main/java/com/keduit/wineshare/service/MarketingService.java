package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.repository.MarketingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketingService {

  private final MarketingRepository marketingRepository;
//  private final ImgFileService imgFileService;

  // 마케팅 저장
  public Long saveMarketing(MarketingDTO marketingDTO) throws IOException{

    Marketing marketing = marketingDTO.createMarketing();
    marketingRepository.save(marketing);
    return marketing.getId();
  }

  // 마케팅은 상세조회 없고 누르면 링크다고 날아갈거임
  public MarketingDTO getMarketingDtl(Long marketingId){
    Marketing marketing = marketingRepository.findById(marketingId).orElseThrow(EntityNotFoundException::new);
    MarketingDTO marketingDTO = MarketingDTO.of(marketing);

    return marketingDTO;
  }

  // 마케팅 수정
  public Long updateMarketing(MarketingDTO marketingDTO) throws IOException{
    Marketing marketing = marketingRepository.findById(marketingDTO.getId())
        .orElseThrow(EntityNotFoundException::new);

    System.out.println("===============4");
    // 이미지 필드 설정
    marketing.setMarketImgName(marketingDTO.getMarketImgName());
    marketing.setMarketImgUrl(marketingDTO.getMarketImgUrl());
    marketing.setMarketOriImgName(marketingDTO.getMarketOriImgName());

    marketing.updateMarketing(marketingDTO);

    System.out.println("===================5");

    return marketing.getId();
  }

  // 마케팅 삭제
  public void deleteMarketing(Long marketingId){
    marketingRepository.deleteById(marketingId);
  }

  // 업장별 페이지와 조회
  @Transactional(readOnly = true)
  public Page<Marketing> getMarketingPageByMarketCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, Pageable pageable){
    return marketingRepository.getMarketingPageByMarketCategory(marketingSearchDTO, marketCategory, pageable);
  }

  // 행사별 페이지와 조회
  @Transactional(readOnly = true)
  public Page<Marketing> getMarketingPageByEventOrNot(MarketingSearchDTO marketingSearchDTO, EventOrNot eventOrNot, Pageable pageable){
    return marketingRepository.getMarketingPageByEventOrNot(marketingSearchDTO, eventOrNot, pageable);
  }

  // 관리자용 마케팅 전체 조회
  @Transactional(readOnly = true)
  public Page<MarketingDTO> getMarketingPage(MarketingSearchDTO marketingSearchDTO, Pageable pageable){
    return marketingRepository.getMarketingPage(marketingSearchDTO, pageable);
  }

  @Transactional(readOnly = true)
  public List<MarketingDTO> getNewMarket(){
    return marketingRepository.getNewMarket();
  }

  // 관리자용 마케팅 행사여부/업장별 조회
  @Transactional(readOnly = true)
  public Page<Marketing> getMarketingPageByEventAndCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory,
                                                            EventOrNot eventOrNot, Pageable pageable){
    return marketingRepository.getMarketingPageByEventAndCategory(marketingSearchDTO, marketCategory, eventOrNot, pageable);
  }

}
