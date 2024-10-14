package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.EventOrNot;
import com.keduit.wineshare.constant.MarketCategory;
import com.keduit.wineshare.dto.MarketingSearchDTO;
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
import java.util.List;

public class MarketingRepositoryCustomImpl implements MarketingRepositoryCustom{

  private JPAQueryFactory queryFactory;

  public MarketingRepositoryCustomImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

  // 업장명으로 검색
  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("T", searchType)){
      return QMarketing.marketing.marketingTitle.like("%" + searchQuery + "%");
    }
    return null;
  }


  // 전체 마케팅 조회  관리자용은 필터는 있되 정렬 옵션은 뺌 그냥 최신순
  @Override
  public Page<Marketing> getMarketingPage(MarketingSearchDTO marketingSearchDTO, Pageable pageable) {
    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));



    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 카운트
    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  // 업장별 분류
  @Override
  public Page<Marketing> getMarketingPageByMarketCategory(MarketingSearchDTO marketingSearchDTO, MarketCategory marketCategory, Pageable pageable) {
    // 기본 쿼리 생성
    JPAQuery<Marketing> query = queryFactory.selectFrom(QMarketing.marketing)
        .join(QMarketing.marketing.member , QMember.member) // 멤버랑 조인
        .leftJoin(QMember.member.wineDevelops, QWineDevelop.wineDevelop)  // 와인디벨롭이랑 조인
        .where(QMarketing.marketing.marketCategory.eq(marketCategory))  // 마케팅 상태 필터
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));

    // 정렬 추가 옵션기본을 활동많은순으로
    if(marketingSearchDTO.getSortOrder().equals("activity")){
      query.orderBy(QWineDevelop.wineDevelop.count().desc()); // 와인디벨롭 많은순으로 정렬(활동많이한순)
    }else{
      query.orderBy(QMarketing.marketing.id.desc());  // 최신순 정렬
    }

    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

//    Long total = queryFactory
//        .select(Wildcard.count)
//        .from(QMarketing.marketing)
//        .where(QMarketing.marketing.marketCategory.eq(marketCategory))
//        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
//        .fetchOne();

    // 전체 카운트
    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .join(QMarketing.marketing.member, QMember.member) // Member와 조인
        .leftJoin(QMember.member.wineDevelops, QWineDevelop.wineDevelop) // WineDevelop와 조인
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
        .join(QMarketing.marketing.member , QMember.member) // 멤버랑 조인
        .leftJoin(QMember.member.wineDevelops, QWineDevelop.wineDevelop)  // 와인디벨롭이랑 조인
        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))  // 행사 상태 필터링 추가
        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()));

    // 정렬 추가 옵션기본을 활동많은순으로
    if(marketingSearchDTO.getSortOrder().equals("activity")){
      query.orderBy(QWineDevelop.wineDevelop.count().desc()); // 와인디벨롭 많은순으로 정렬(활동많이한순)
    }else{
      query.orderBy(QMarketing.marketing.id.desc());  // 최신순 정렬
    }

    List<Marketing> result = query
        .orderBy(QMarketing.marketing.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

//    Long total = queryFactory
//        .select(Wildcard.count)
//        .from(QMarketing.marketing)
//        .where(QMarketing.marketing.eventOrNot.eq(eventOrNot))
//        .where(searchTypeLike(marketingSearchDTO.getSearchType(), marketingSearchDTO.getSearchQuery()))
//        .fetchOne();

    // 전체 카운트
    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMarketing.marketing)
        .join(QMarketing.marketing.member, QMember.member) // Member와 조인
        .leftJoin(QMember.member.wineDevelops, QWineDevelop.wineDevelop) // WineDevelop와 조인
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


}
