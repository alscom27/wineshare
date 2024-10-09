package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

// 동적쿼리로 검색 페이징 추가할거임
public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {

}
