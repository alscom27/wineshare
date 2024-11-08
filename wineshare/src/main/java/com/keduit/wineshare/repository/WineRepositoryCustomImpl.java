package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.QWineDTO;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class WineRepositoryCustomImpl implements WineRepositoryCustom {
  private JPAQueryFactory queryFactory;

  public WineRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  //N 와인이름 C 국가이름 W 작성자
  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("N", searchType)){
      return QWine.wine.wineName.like("%"+searchQuery+"%");
    }else if(StringUtils.equals("C", searchType)){
      return QWine.wine.country.like("%"+searchQuery+"%");
    }else if(StringUtils.equals("W", searchType)){
      return QWine.wine.member.nickname.like("%"+searchQuery+"%");
    }
    return null;
  }

  // 와인타입 필터
  private BooleanExpression searchWineTypeEq(WineType searchWineType) {
    return searchWineType == null ? null : QWine.wine.wineType.eq(searchWineType);
  }

  // 평균별점 계산하기(전문가만)
  private NumberExpression<Double> averageRating() {
    return QWineDevelop.wineDevelop.expertRating.avg();
  }

  // 인기순
  private NumberExpression<Long> cellarCount() {
    return QCellarWine.cellarWine.wine.id.count();
  }

  // 정렬...
  private OrderSpecifier<?> sortBy(String sortBy) {
    if (sortBy == null) {
      return QWine.wine.regTime.desc(); // 기본정렬(최신순)
    } else if (StringUtils.equals("priceAsc", sortBy)) {
      return QWine.wine.price.asc(); // 가격 오름차순
    } else if (StringUtils.equals("priceDesc", sortBy)) {
      return QWine.wine.price.desc(); // 가격 내림차순
    } else if (StringUtils.equals("ratingAsc", sortBy)) {
      // 평균 별점 오름차순
      return averageRating().asc();
    } else if (StringUtils.equals("ratingDesc", sortBy)) {
      // 평균 별점 내림차순
      return averageRating().desc();
    } else if (StringUtils.equals("cellarDesc", sortBy)) {
      return cellarCount().desc();
    } else {
      return QWine.wine.regTime.desc(); // 기본정렬(최신순)
    }
  }

  // 가격 범위 조건
  private BooleanExpression priceBetween(Integer minPrice, Integer maxPrice) {
    if (minPrice != null && maxPrice != null) {
      return QWine.wine.price.between(minPrice, maxPrice); // 최소, 최대 잘 설정됐을 때
    } else if (minPrice != null) {
      return QWine.wine.price.goe(minPrice); // 최소 가격만 있는 경우
    } else if (maxPrice != null) {
      return QWine.wine.price.loe(maxPrice); // 최대 가격만 있는 경우
    }
    return null; // 가격 조건이 없는 경우
  }


  // 나중에 필터나 정렬 조건으로 추가해주기
  @Override
  public Page<WineDTO> getWinePage(WineSearchDTO wineSearchDTO, Pageable pageable) {
    QWine wine = QWine.wine;
    QWineDevelop wineDevelop = QWineDevelop.wineDevelop;
    QCellarWine cellarWine = QCellarWine.cellarWine;

    List<WineDTO> result = queryFactory
        .select(
            new QWineDTO(
                wine.id,
                wine.wineName,
                wine.country,
                wine.region,
                wine.price,
                wine.wineType,
                wine.member.id,
                wine.wineImg,
                wine.member.nickname
            )
        )
        .from(wine)
        .leftJoin(wine.wineDevelops, wineDevelop)
        .leftJoin(wine.cellarWines, cellarWine) // 셀러와의 조인 추가
        .where(searchWineTypeEq(wineSearchDTO.getSearchWineType()))
        .where(searchTypeLike(wineSearchDTO.getSearchType(), wineSearchDTO.getSearchQuery()))
        .where(priceBetween(wineSearchDTO.getMinPrice(), wineSearchDTO.getMaxPrice()))
        .groupBy(wine.id) // 그룹화
        .orderBy(sortBy(wineSearchDTO.getSortBy())) // 셀러 카운트로 정렬
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(wine)
        .leftJoin(wine.wineDevelops, wineDevelop)
        .where(searchWineTypeEq(wineSearchDTO.getSearchWineType()))
        .where(priceBetween(wineSearchDTO.getMinPrice(), wineSearchDTO.getMaxPrice()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  // 1. 타입이 같은것 중 전문가 평점이 높은것...
  @Override
  public Wine findSameTypeMostRating(Wine wine) {
    QWine qWine = QWine.wine;
    QWineDevelop qWineDevelop = QWineDevelop.wineDevelop;

    // 타입이 같은 와인 중에서 각 와인의 expertRating의 평균을 계산
    Wine recommendedWine = queryFactory
        .select(qWine)
        .from(qWine)
        .join(qWine.wineDevelops, qWineDevelop) // WineDevelop과 조인
        .where(qWine.wineType.eq(wine.getWineType()) // 같은 타입 필터링
            .and(qWine.ne(wine))) // 현재 와인은 제외하기
        .groupBy(qWine.id) // 와인 ID로 그룹화
        .orderBy(qWineDevelop.expertRating.avg().desc()) // 평균 expertRating으로 내림차순 정렬
        .fetchFirst(); // 가장 첫 번째 결과(가장 높은 평균 expertRating)를 가져옴

    // 추천 와인이 null인 경우 현재 와인 반환
    if (recommendedWine == null) {
      return wine; // 현재 와인 반환
    }

    return recommendedWine; // 추천 와인 반환
  }
  // 2. 국가가 같은 와인 중 전문가 평점이 높은것
  @Override
  public Wine findSameCountryMostRating(Wine wine) {
    QWine qWine = QWine.wine;
    QWineDevelop qWineDevelop = QWineDevelop.wineDevelop;

    // 같은 국가의 와인 중에서 각 와인의 expertRating의 평균을 계산
    Wine recommendedWine = queryFactory
        .select(qWine)
        .from(qWine)
        .join(qWine.wineDevelops, qWineDevelop) // WineDevelop과 조인
        .where(qWine.country.eq(wine.getCountry()) // 같은 타입 필터링
            .and(qWine.ne(wine))) // 현재 와인은 제외하기
        .groupBy(qWine.id) // 와인 ID로 그룹화
        .orderBy(qWineDevelop.expertRating.avg().desc()) // 평균 expertRating으로 내림차순 정렬
        .fetchFirst(); // 가장 첫 번째 결과(가장 높은 평균 expertRating)를 가져옴
    // 추천 와인이 null인 경우 현재 와인 반환
    if (recommendedWine == null) {
      return wine; // 현재 와인 반환
    }

    return recommendedWine; // 추천 와인 반환
  }
  // 3. 가장 높은 카운트의 아로마원을 제일 많이 가지고 있는 와인
  @Override
  public Wine findMostFrequentAromaOneWine(Wine wine) {
    QWine qWine = QWine.wine;
    QWineDevelop qWineDevelop = QWineDevelop.wineDevelop;

    // 현재 와인의 WineDevelop에서 가장 많이 나오는 aromaOne 찾기
    String mostFrequentAromaOne = queryFactory
        .select(qWineDevelop.aromaOne)
        .from(qWineDevelop)
        .where(qWineDevelop.wine.eq(wine))
        .groupBy(qWineDevelop.aromaOne)
        .orderBy(qWineDevelop.aromaOne.count().desc())
        .fetchFirst();

    // 찾은 aromaOne을 가진 다른 와인들 중에서 aromaOne의 카운트가 가장 높은 와인 찾기
    Wine recommendedWine = queryFactory
        .select(qWine)
        .from(qWine)
        .join(qWine.wineDevelops, qWineDevelop)
        .where(qWineDevelop.aromaOne.eq(mostFrequentAromaOne)
            .and(qWine.ne(wine))) // 현재 와인은 제외하기
        .groupBy(qWine.id)
        .orderBy(qWineDevelop.aromaOne.count().desc())
        .fetchFirst();
    // 추천 와인이 null인 경우 현재 와인 반환
    if (recommendedWine == null) {
      return wine; // 현재 와인 반환
    }

    return recommendedWine; // 추천 와인 반환
  }
  // 가장 높은 카운트의 푸드원을 제일 많이 가지고 있는 와인
  @Override
  public Wine findMostFrequentFoodOneWine(Wine wine) {
    QWine qWine = QWine.wine;
    QWineDevelop qWineDevelop = QWineDevelop.wineDevelop;

    // 현재 와인의 WineDevelop에서 가장 많이 나오는 foodOne 찾기
    String mostFrequentFoodOne = queryFactory
        .select(qWineDevelop.foodOne)
        .from(qWineDevelop)
        .where(qWineDevelop.wine.eq(wine))
        .groupBy(qWineDevelop.foodOne)
        .orderBy(qWineDevelop.foodOne.count().desc())
        .fetchFirst();

    // 찾은 foodOne을 가진 다른 와인들 중에서 foodOne의 카운트가 가장 높은 와인 찾기
    Wine recommendedWine = queryFactory
        .select(qWine)
        .from(qWine)
        .join(qWine.wineDevelops, qWineDevelop)
        .where(qWineDevelop.foodOne.eq(mostFrequentFoodOne)
            .and(qWine.ne(wine))) // 현재 와인은 제외하기
        .groupBy(qWine.id)
        .orderBy(qWineDevelop.foodOne.count().desc())
        .fetchFirst();

    // 추천 와인이 null인 경우 현재 와인 반환
    if (recommendedWine == null) {
      return wine; // 현재 와인 반환
    }

    return recommendedWine; // 추천 와인 반환
  }

  // 여기부터 메인에 들어갈 추천와인
  // 별점 상위 3개
  @Override
  public List<Wine> findRecommendedRatingWines() {
    QWine qWine = QWine.wine;
    QWineDevelop qWineDevelop = QWineDevelop.wineDevelop;


    // 타입이 같은 와인 중에서 각 와인의 expertRating의 평균을 계산
    return queryFactory
        .select(qWine)
        .from(qWine)
        .join(qWine.wineDevelops, qWineDevelop) // WineDevelop과 조인
        .groupBy(qWine.id) // 와인 ID로 그룹화
        .orderBy(qWineDevelop.expertRating.avg().desc()) // 평균 expertRating으로 내림차순 정렬
        .limit(3)
        .fetch(); // 가장 첫 번째 결과(가장 높은 평균 expertRating)를 가져옴

  }

  // 인기순(셀러에 담긴 것) 상위 3개
  @Override
  public List<Wine> findRecommendedCellarWines() {
    QWine wine = QWine.wine;
    QCellarWine cellarWine = QCellarWine.cellarWine;

    return queryFactory
        .select(wine)
        .from(wine)
        .join(wine.cellarWines, cellarWine)
        .groupBy(wine.id)
        .orderBy(cellarWine.wine.wineName.count().desc())
        .limit(3)
        .fetch();
  }

  // 최신순 상위 3개
  @Override
  public List<Wine> findRecommendedDescWines() {
    QWine wine = QWine.wine;

    return queryFactory
        .select(wine)
        .from(wine)
        .orderBy(wine.regTime.desc())
        .limit(3)
        .fetch();
  }


}

