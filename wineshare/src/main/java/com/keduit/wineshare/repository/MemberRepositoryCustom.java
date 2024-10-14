package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

  // 관리자용

  // member 전체조회
  Page<Member> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageable);

  // 회원 타입별 조회
  Page<Member> getMemberPageByMemberType(MemberSearchDTO memberSearchDTO, MemberType memberType, Pageable pageable);

  // 회원 탈퇴여부별 조회
  Page<Member> getMemberPageByWithdrawStatus(MemberSearchDTO memberSearchDTO, WithdrawStatus withdrawStatus, Pageable pageable);
}
