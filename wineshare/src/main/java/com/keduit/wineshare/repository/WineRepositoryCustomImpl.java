package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.QWineDTO;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.QWine;
import com.keduit.wineshare.entity.QWineDevelop;
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

  public WineRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

  // 와인타입 필터
  private BooleanExpression searchWineTypeEq(WineType searchWineType) {
    return searchWineType == null ? null : QWine.wine.wineType.eq(searchWineType);
  }

  // 평균별점 계산하기(전문가만)
  private NumberExpression<Double> averageRating() {
    return QWineDevelop.wineDevelop.expertRating.avg();
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
                wine.wineImg
            )
        )
        .from(wine)
        .leftJoin(wine.wineDevelops, wineDevelop)
        .where(searchWineTypeEq(wineSearchDTO.getSearchWineType()))
        .where(priceBetween(wineSearchDTO.getMinPrice(), wineSearchDTO.getMaxPrice()))
        .groupBy(wine.id)
        .orderBy(sortBy(wineSearchDTO.getSortBy()))
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
}
