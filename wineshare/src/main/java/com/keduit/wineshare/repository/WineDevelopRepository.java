package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WineDevelopRepository extends JpaRepository<WineDevelop, Long>, QuerydslPredicateExecutor<WineDevelop>, WineDevelopRepositoryCustom{

  List<WineDevelop> findByWine(Wine wine);

  @Query("SELECT wd.aromaOne, COUNT(wd.aromaOne) FROM WineDevelop wd WHERE wd.wine = :wine GROUP BY wd.aromaOne ORDER BY COUNT(wd.aromaOne) DESC")
  List<Object[]> countAromaOneByWine(@Param("wine") Wine wine);

  @Query("SELECT wd.aromaTwo, COUNT(wd.aromaTwo) FROM WineDevelop wd WHERE wd.wine = :wine GROUP BY wd.aromaTwo ORDER BY COUNT(wd.aromaTwo) DESC")
  List<Object[]> countAromaTwoByWine(@Param("wine") Wine wine);

  @Query("SELECT wd.foodOne, COUNT(wd.foodOne) FROM WineDevelop wd WHERE wd.wine = :wine GROUP BY wd.foodOne ORDER BY COUNT(wd.foodOne) DESC")
  List<Object[]>  countFoodOneByWine(@Param("wine") Wine wine);

  @Query("SELECT wd.foodTwo, COUNT(wd.foodTwo) FROM WineDevelop wd WHERE wd.wine = :wine GROUP BY wd.foodTwo ORDER BY COUNT(wd.foodTwo) DESC")
  List<Object[]>  countFoodTwoByWine(@Param("wine") Wine wine);

  WineDevelop findWineDevelopById(Long id);

  Long countByWineId(Long id);
}
