package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.CellarDetailDTO;
import com.keduit.wineshare.dto.QCellarDetailDTO;
import com.keduit.wineshare.entity.QCellarWine;
import com.keduit.wineshare.entity.QWine;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class CellarWineRepositoryCustomImpl implements CellarWineRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public CellarWineRepositoryCustomImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

  @Override
  public Page<CellarDetailDTO> getCellarWines(Pageable pageable, Long cellarId) {

    if (cellarId == null) {
      throw new IllegalArgumentException("cellarId must not be null");
    }

    QCellarWine cellarWine = QCellarWine.cellarWine;
    QWine wine = QWine.wine;

    List<CellarDetailDTO> result = queryFactory
        .select(new QCellarDetailDTO(
            cellarWine.id,
            wine.wineName,
            wine.wineImg,
            wine.wineType,
            wine.id
        ))
        .from(cellarWine)
        .join(cellarWine.wine, wine)
        .where(cellarWine.cellar.id.eq(cellarId))
        .orderBy(cellarWine.regTime.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(cellarWine)
        .where(cellarWine.cellar.id.eq(cellarId))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }
}
