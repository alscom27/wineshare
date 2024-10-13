package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

  // 관리자용 member 전체조회
  Page<Member> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageable);

}
