package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.QMember;
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

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public MemberRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  // 검색타입 N = 이름 E = email k = 닉네임
  private BooleanExpression searchTypeLike(String searchType, String searchQuery){
    if(StringUtils.equals("N", searchType)){
      return QMember.member.name.like("%" + searchQuery + "%");
    }else if(StringUtils.equals("E", searchType)){
      return QMember.member.email.like("%" + searchQuery + "%");
    }else if(StringUtils.equals("K", searchType)){
      return QMember.member.nickname.like("%" + searchQuery + "%");
    }
    return null;
  }

  // 멤버 페이지네이션과함께 리스트 조회
  @Override
  public Page<Member> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageable) {
    // 검색 조건
    BooleanExpression searchCondition = searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery());

    // 멤버 리스트 조회
    List<Member> result = queryFactory
        .selectFrom(QMember.member)
        .where(searchCondition)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 멤버 수 카운트
    Long total = queryFactory
        .selectFrom(QMember.member)
        .where(searchCondition)
        .fetchCount();

    // Page 객체 생성하여 반환
    return new PageImpl<>(result, pageable, total);
  }

  // 회원 타입별조회
  @Override
  public Page<Member> getMemberPageByMemberType(MemberSearchDTO memberSearchDTO, MemberType memberType, Pageable pageable) {
    JPAQuery<Member> query = queryFactory.selectFrom(QMember.member)
        .where(QMember.member.memberType.eq(memberType))
        .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()));

    List<Member> result = query
        .orderBy(QMember.member.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMember.member)
        .where(QMember.member.memberType.eq(memberType))
        .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  // 회원 탈퇴여부별 조회
  @Override
  public Page<Member> getMemberPageByWithdrawStatus(MemberSearchDTO memberSearchDTO, WithdrawStatus withdrawStatus, Pageable pageable) {
    JPAQuery<Member> query = queryFactory.selectFrom(QMember.member)
        .where(QMember.member.withdrawStatus.eq(withdrawStatus))
        .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()));

    List<Member> result = query
        .orderBy(QMember.member.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(QMember.member)
        .where(QMember.member.withdrawStatus.eq(withdrawStatus))
        .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(result, pageable, total);
  }

  // 정렬 추가(타입별, 회원탈퇴여부별



}
