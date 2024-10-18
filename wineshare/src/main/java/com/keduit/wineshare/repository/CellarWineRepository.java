package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.CellarDetailDTO;
import com.keduit.wineshare.entity.CellarWine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CellarWineRepository extends JpaRepository<CellarWine, Long>, QuerydslPredicateExecutor<CellarWine>, CellarWineRepositoryCustom {


//  CellarWine findByCellarIdAndWineId(Long cellarId, Long wineId);

  @Query("SELECT cw FROM CellarWine cw WHERE cw.cellar.id = :cellarId AND cw.wine.id = :wineId")
  CellarWine findByCellarIdAndWineId(@Param("cellarId") Long cellarId, @Param("wineId") Long wineId);

  @Query("select new com.keduit.wineshare.dto.CellarDetailDTO(cw.id, w.wineName, w.wineImg, w.wineType, w.id) " +
          "from CellarWine cw " +
          "join cw.wine w " +
          "where cw.cellar.id = :cellarId " +
          "order by cw.regTime desc")
  List<CellarDetailDTO> findCellarDetailDTOList(@Param("cellarId") Long cellarId);

}
