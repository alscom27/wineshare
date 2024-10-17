package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.QWineDevelopDTO;
import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.QWineDevelop;
import com.keduit.wineshare.entity.WineDevelop;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

public class WineDevelopRepositoryCustomImpl implements WineDevelopRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public WineDevelopRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }


  @Override
  public Page<WineDevelopDTO> getDevelopPageByWine(Long wineId, Pageable pageable) {
    if (wineId == null) {
      throw new IllegalArgumentException("wineId must not be null");
    }
    QWineDevelop wineDevelop = QWineDevelop.wineDevelop;


    // 쿼리 작성
    List<WineDevelopDTO> content = queryFactory
        .select(
            new QWineDevelopDTO(
                wineDevelop.id,
                wineDevelop.wine.id,
                wineDevelop.member.id,
                wineDevelop.expertRating,
                wineDevelop.expertComment,
                wineDevelop.sweetness,
                wineDevelop.acidity,
                wineDevelop.body,
                wineDevelop.tannin,
                wineDevelop.fizz,
                wineDevelop.aromaOne,
                wineDevelop.aromaTwo,
                wineDevelop.foodOne,
                wineDevelop.foodTwo
                )) // DTO로 매핑
        .from(wineDevelop)
        .where(wineDevelop.wine.id.eq(wineId)) // 와인 ID로 필터링
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(wineDevelop.regTime.desc())
        .fetch();

    // 전체 개수 조회
    Long total = queryFactory
        .select(Wildcard.count)
        .from(wineDevelop)
        .where(wineDevelop.wine.id.eq(wineId))
        .fetchOne();

    if (content.isEmpty()) {
      // 예외 발생
      // throw new EntityNotFoundException("No WineDevelop found for wineId: " + wineId);

      // 빈 페이지 반환
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    return new PageImpl<>(content, pageable, total != null ? total : 0); // Page 객체 반환
  }
}
