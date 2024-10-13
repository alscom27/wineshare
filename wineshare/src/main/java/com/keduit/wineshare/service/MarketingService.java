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

@Service
@Transactional
@RequiredArgsConstructor
public class MarketingService {

  private final MarketingRepository marketingRepository;
  private final ImgFileService imgFileService;

  // 마케팅 저장
  public Long saveMarketing(MarketingDTO marketingDTO) throws IOException{

    MultipartFile imageFile = marketingDTO.getMarketImgFile();
    if(imageFile != null && !imageFile.isEmpty()){
      String imagePath = imgFileService.saveMarketingImg(imageFile);
      marketingDTO.setMarketImg(imagePath);
    }
    // 이미지가 null일 가정은 우선 제외함

    Marketing marketing = marketingDTO.createMarketing();
    marketingRepository.save(marketing);
    return marketing.getId();
  }

  // 마케팅은 상세조회 없고 누르면 링크다고 날아갈거임

  public Long updateMarketing(MarketingDTO marketingDTO) throws IOException{
    Marketing marketing = marketingRepository.findById(marketingDTO.getId())
        .orElseThrow(EntityNotFoundException::new);

    // 이미지 처리
    MultipartFile imageFile = marketingDTO.getMarketImgFile();
    if(imageFile != null && !imageFile.isEmpty()){
      // 이미지가 업로드 된 경우 기존이미지를 업데이트
      String imagePath = imgFileService.saveMarketingImg(imageFile);
      marketingDTO.setMarketImg(imagePath);
    }else{
      // 기존 이미지 유지
      marketingDTO.setMarketImg(marketing.getMarketImg());
    }

    marketing.updateMarketing(marketingDTO);
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
  public Page<Marketing> getMarketingPage(MarketingSearchDTO marketingSearchDTO, Pageable pageable){
    return marketingRepository.getMarketingPage(marketingSearchDTO, pageable);
  }

}
