package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.QWineReviewDTO;
import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.QWineReview;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class WineReviewRepositoryCustomImpl implements WineReviewRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public WineReviewRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<WineReviewDTO> getReviewPageByWine(Long wineId, Pageable pageable) {
    QWineReview wineReview = QWineReview.wineReview;

    List<WineReviewDTO> content = queryFactory
        .select(
            new QWineReviewDTO(
                wineReview.id,
                wineReview.wine.id,
                wineReview.member.id,
                wineReview.regularRating,
                wineReview.regularReview
            )
        )
        .from(wineReview)
        .where(wineReview.wine.id.eq(wineId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(wineReview.regTime.desc())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(wineReview)
        .where(wineReview.wine.id.eq(wineId))
        .fetchOne();
    return new PageImpl<>(content, pageable, total);
  }
}
