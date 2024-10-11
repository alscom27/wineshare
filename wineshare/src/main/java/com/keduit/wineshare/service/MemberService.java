package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
