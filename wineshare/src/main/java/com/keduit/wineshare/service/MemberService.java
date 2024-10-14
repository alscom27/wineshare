package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public Member saveMember(Member member) {
    validateMember(member);
    return memberRepository.save(member);
  }

  private void validateMember(Member member) {
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if(findMember != null) {
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }

  public Member findByEmail(String email) {
    return memberRepository.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email);

    if(member == null) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    if(member.getWithdrawStatus() == WithdrawStatus.LEAVE){
      throw new UsernameNotFoundException("탈퇴한 회원입니다.");
    }

    return User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getMemberType().toString())
        .build();
  }

  // 관리자용 페이지와 조회
  @Transactional(readOnly = true)
  public Page<Member> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageaple){
    return memberRepository.getMemberPage(memberSearchDTO, pageaple);
  }

  // 회원 타입별
  @Transactional(readOnly = true)
  public Page<Member> getMemberPageByMemberType(MemberSearchDTO memberSearchDTO, MemberType memberType, Pageable pageable){
    return memberRepository.getMemberPageByMemberType(memberSearchDTO, memberType, pageable);
  }

  // 회원탈퇴여부별
  @Transactional(readOnly = true)
  public Page<Member> getMemberPageByWithdrawStatus(MemberSearchDTO memberSearchDTO, WithdrawStatus withdrawStatus, Pageable pageable){
    return memberRepository.getMemberPageByWithdrawStatus(memberSearchDTO, withdrawStatus, pageable);
  }

}
