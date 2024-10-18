package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.dto.BoardSearchDTO;
import com.keduit.wineshare.dto.QBoardDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.QBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

  private JPAQueryFactory queryFactory;

  public BoardRepositoryCustomImpl(EntityManager em){
    this.queryFactory = new JPAQueryFactory(em);
  }

  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("T", searchType)){
      return QBoard.board.boardTitle.like("%" + searchQuery + "%");
    }else if(StringUtils.equals("C", searchType)){
      return QBoard.board.boardContent.like("%" + searchQuery + "%");
    }else if(StringUtils.equals("W", searchType)){
      return QBoard.board.member.nickname.like("%" + searchQuery + "%");
    }
    return null;
  }

  // 관리자용 - 보드스테이터스 필터
  private BooleanExpression searchBoardStatusEq(BoardStatus searchBoardStatus){
    return searchBoardStatus == null ? null : QBoard.board.boardStatus.eq(searchBoardStatus);
  }


  // 관리자 전체 조회
  @Override
  public Page<BoardDTO> getBoardPage(BoardSearchDTO boardSearchDTO, Pageable pageable) {

    QBoard board = QBoard.board;

    List<BoardDTO> result = queryFactory
        .select(
            new QBoardDTO(
                board.id,
                board.boardStatus,
                board.boardTitle,
                board.regBy,
                board.regTime,
                board.member.nickname
            )
        )
        .from(board)
        .where(searchBoardStatusEq(boardSearchDTO.getSearchBoardStatus()))
        .where(searchTypeLike(boardSearchDTO.getSearchType(), boardSearchDTO.getSearchQuery()))
        .orderBy(board.regTime.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(board)
        .where(searchBoardStatusEq(boardSearchDTO.getSearchBoardStatus()))
        .where(searchTypeLike(boardSearchDTO.getSearchType(), boardSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }



  @Override
  // 게시판 상태별 페이지와 조회
  public Page<Board> getBoardPageByStatus(BoardSearchDTO boardSearchDTO, BoardStatus boardStatus, Pageable pageable) {
    //기본 쿼리생성
    JPAQuery<Board> query = queryFactory.selectFrom(QBoard.board)
        .where(QBoard.board.boardStatus.eq(boardStatus)) // 게시판 상태 필터링 추가
        .where(searchTypeLike(boardSearchDTO.getSearchType(), boardSearchDTO.getSearchQuery()));


    List<Board> result = query
        .orderBy(QBoard.board.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QBoard.board)
        .where(QBoard.board.boardStatus.eq(boardStatus))
        .where(searchTypeLike(boardSearchDTO.getSearchType(), boardSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }


}
