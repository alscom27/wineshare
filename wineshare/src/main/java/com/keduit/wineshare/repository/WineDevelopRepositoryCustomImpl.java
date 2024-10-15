package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.QWineDevelopDTO;
import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.QWineDevelop;
import com.keduit.wineshare.entity.WineDevelop;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class WineDevelopRepositoryCustomImpl implements WineDevelopRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public WineDevelopRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<WineDevelopDTO> getDevelopPageByWine(Long wineId, Pageable pageable) {
    QWineDevelop wineDevelop = QWineDevelop.wineDevelop;

    // 쿼리 작성
    List<WineDevelopDTO> content = queryFactory
        .select(
            new QWineDevelopDTO(
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
        .fetch();

    // 전체 개수 조회
    Long total = queryFactory
        .select(Wildcard.count)
        .from(wineDevelop)
        .where(wineDevelop.wine.id.eq(wineId))
        .fetchOne();

    return new PageImpl<>(content, pageable, total); // Page 객체 반환
  }
}
