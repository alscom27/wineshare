package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.entity.Marketing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketingRepositoryCustom {

  // 전체 페이지와같이
  Page<MarketingDTO> getMarketingPage(MarketingSearchDTO marketingSearchDTO, Pageable pageable);

  // 일단 게시판이랑 비슷한 형태고 가고있어서 안되면 다 나가리...
  // 업장별로 보여주는거
  Page<Marketing> getMarketingPageByMarketCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, Pageable pageable);

  // 행사중인거만 보여주는거
  Page<Marketing> getMarketingPageByEventOrNot(MarketingSearchDTO marketingSearchDTO, EventOrNot eventOrNot, Pageable pageable);

  // 행사여부 업장별 필터링
  Page<Marketing> getMarketingPageByEventAndCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory,
                                                     EventOrNot eventOrNot, Pageable pageable);


}
