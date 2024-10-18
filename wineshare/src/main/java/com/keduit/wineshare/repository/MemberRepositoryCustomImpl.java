package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.dto.QMemberDTO;
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

  private BooleanExpression searchMemberTypeEq(MemberType searchMemberType){
    return searchMemberType == null ? null : QMember.member.memberType.eq(searchMemberType);
  }
  private BooleanExpression searchWithdrawStatusEq(WithdrawStatus searchWithdrawStatus){
    return searchWithdrawStatus == null ? null : QMember.member.withdrawStatus.eq(searchWithdrawStatus);
  }


  // 관리자용 멤버조회
  @Override
  public Page<MemberDTO> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageable) {
      QMember member = QMember.member;

      List<MemberDTO> result = queryFactory
          .select(
              new QMemberDTO(
                  member.id,
                  member.memberType,
                  member.email,
                  member.name,
                  member.withdrawStatus
              )
          )
          .from(member)
          .where(searchMemberTypeEq(memberSearchDTO.getMemberType()))
          .where(searchWithdrawStatusEq(memberSearchDTO.getWithdrawStatus()))
          .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()))
          .orderBy(member.regTime.desc())
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();

      Long total = queryFactory
          .select(Wildcard.count)
          .from(member)
          .where(searchMemberTypeEq(memberSearchDTO.getMemberType()))
          .where(searchWithdrawStatusEq(memberSearchDTO.getWithdrawStatus()))
          .where(searchTypeLike(memberSearchDTO.getSearchType(), memberSearchDTO.getSearchQuery()))
          .fetchOne();

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
