package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.dto.WineSearchDTO;
import com.keduit.wineshare.entity.Wine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface WineRepository extends JpaRepository<Wine, Long>, QuerydslPredicateExecutor<Wine>, WineRepositoryCustom, CrudRepository<Wine, Long> {


  Wine findByWineName(String wineName);

  // 최소가격 조회
  @Query("SELECT MIN(w.price) FROM Wine w")
  Double findMinPrice();

  // 최대가격 조회
  @Query("SELECT MAX(w.price) FROM Wine w")
  Double findMaxPrice();


}

