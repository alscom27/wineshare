package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.dto.QMarketingDTO;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.QMarketing;
import com.keduit.wineshare.entity.QMember;
import com.keduit.wineshare.entity.QWineDevelop;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class MarketingRepositoryCustomImpl implements MarketingRepositoryCustom{

  private JPAQueryFactory queryFactory;

  public MarketingRepositoryCustomImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

  // 업장명으로 검색
  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("T", searchType)){
      return QMarketing.marketing.marketingTitle.like("%" + searchQuery + "%");
    }
//    else if(StringUtils.equals("N", searchType)){
//      return QMarketing.marketing.member.nickname.like("%" + searchQuery + "%");
//    }
    return null;
  }
  // 관리자용 - 마켓 종류 필터
  private BooleanExpression searchMarketCategoryEq(MarketCategory searchMarketCategory){
    return searchMarketCategory == null ? null : QMarketing.marketing.marketCategory.eq(searchMarketCategory);
  }

  // 관리자용 - 행사 필터
  private BooleanExpression searchEventOrNotEq(EventOrNot searchEventOrNot){
    return searchEventOrNot == null ? null : QMarketing.marketing.eventOrNot.eq(searchEventOrNot);
  }



  // 관리자용 전체 조회
  @Override
  public Page<MarketingDTO> getMarketingPage(MarketingSearchDTO marketingSearchDTO, Pageable pageable) {

    QMarketing marketing = QMarketing.marketing;

    List<MarketingDTO> result = queryFactory
        .select(
            new QMarketingDTO(
                marketing.id,
                marketing.marketCategory,
                marketing.marketingTitle,
                marketing.eventOrNot,
                marketing.member.nickname
            )
        )
        .from(marketing)
        .where(searchMarketCategoryEq(marketingSearchDTO.getMarketCategory()))
        .where(searchEventOrNotEq(marketingSearchDTO.getEventOrNot()))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .orderBy(marketing.regTime.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(marketing)
        .where(searchMarketCategoryEq(marketingSearchDTO.getMarketCategory()))
        .where(searchEventOrNotEq(marketingSearchDTO.getEventOrNot()))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  // 업장별 분류
  @Override
  public Page<Marketing> getMarketingPageByMarketCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, Pageable pageable) {

    // 기본쿼리
    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .where(QMarketing.marketing.marketCategory.eq(marketCategory))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));

    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .where(QMarketing.marketing.marketCategory.eq(marketCategory))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);


  }

  // 행사별 분류
  @Override
  public Page<Marketing> getMarketingPageByEventOrNot(MarketingSearchDTO marketingSearchDTO, EventOrNot eventOrNot, Pageable pageable) {

    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));

    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  @Override
  public Page<Marketing> getMarketingPageByEventAndCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, EventOrNot eventOrNot, Pageable pageable) {
    // 기본 쿼리 생성
    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))  // 행사 상태 필터링 추가
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));

    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .where(QMarketing.marketing.marketCategory.eq(marketCategory))
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);

  }

  // 메인페이지용 최신 마켓 리스트
  @Override
  public List<MarketingDTO> getNewMarket() {
    QMarketing marketing = QMarketing.marketing;
    return queryFactory
        .select(new QMarketingDTO(
            marketing.id,
            marketing.marketCategory,
            marketing.marketingTitle,
            marketing.eventOrNot,
            marketing.member.nickname,
            marketing.marketImgUrl,
            marketing.marketingContent,
            marketing.marketLink
        ))
        .from(marketing)
        .where(QMarketing.marketing.marketCategory.ne(MarketCategory.PUBLIC))
        .orderBy(marketing.regTime.desc())
        .limit(12)
        .fetch();
  }


}
