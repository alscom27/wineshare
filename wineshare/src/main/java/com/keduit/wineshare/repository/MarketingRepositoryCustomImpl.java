package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingSearchDTO;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.entity.QMarketing;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class MarketingRepositoryCustomImpl implements MarketingRepositoryCustom{

  private JPAQueryFactory queryFactory;

  public MarketingRepositoryCustomImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("T", searchType)){
      return QMarketing.marketing.marketingTitle.like("%" + searchQuery + "%");
    }
    return null;
  }

  // 업장별 분류
  @Override
  public Page<Marketing> getMarketingPageByMarketCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, Pageable pageable) {
    // 기본 쿼리 생성
    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .where(QMarketing.marketing.marketCategory.eq(marketCategory))  // 마케팅 상태 필터
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
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }


}
