package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.dto.MemberModifyDTO;
import com.keduit.wineshare.dto.MemberPassModifyDTO;
import com.keduit.wineshare.dto.MemberSearchDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member saveMember(Member member) {
    validateMember(member);
    return memberRepository.save(member);
  }

  public Long updateMember(MemberModifyDTO memberModifyDTO){
    Member member = memberRepository.findById(memberModifyDTO.getId()).orElseThrow(EntityNotFoundException::new);

    member.updateMember(memberModifyDTO);

    memberRepository.save(member);

    return member.getId();
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

//    UsernameNotFoundException 이 컨피그안에서 BadCredentialsException 으로 결국 리턴되서 계속 아이디 또는 비번 에러메세지만 출력됐었음
    if(member == null) {
      throw new BadCredentialsException("사용자를 찾을 수 없습니다.");
    }

    if(member.getWithdrawStatus() == WithdrawStatus.LEAVE){
      throw new BadCredentialsException("탈퇴한 회원입니다.");
    }

    System.out.println("로그인 시 비밀번호 해시: " + member.getPassword());


    return User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getMemberType().toString())
        .build();
  }

  // 비밀번호 변경 서비스에서 검증하는법
  public void changePassword(MemberPassModifyDTO memberPassModifyDTO, String email){
    Member member = memberRepository.findByEmail(email);

    if(member != null){
      // 기존 비밀번호 확인
      System.out.println("changePassword");
      System.out.println("입력한 비밀번호: " + memberPassModifyDTO.getOriginPassword());
      System.out.println("저장된 비밀번호 해시: " + member.getPassword());

      if(!passwordEncoder.matches(memberPassModifyDTO.getOriginPassword(), member.getPassword())){
        throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
      }

      // 비밀번호 확인 검증
      if(!memberPassModifyDTO.getNewPassword().equals(memberPassModifyDTO.getConfirmPassword())){
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다..");
      }

      // 비밀번호 변경
      String encodePassword = passwordEncoder.encode(memberPassModifyDTO.getNewPassword());
      member.setPassword(encodePassword);
      memberRepository.save(member);

    }else{
      throw new EntityNotFoundException("사용자를 찾을 수 없습니다.");
    }
  }

  public boolean checkPassword(MemberPassModifyDTO memberPassModifyDTO, String email) {
    Member member = memberRepository.findByEmail(email);
    if (member != null) {
      System.out.println("입력한 비밀번호: " + memberPassModifyDTO.getOriginPassword());
      System.out.println("저장된 비밀번호 해시: " + member.getPassword());
      return passwordEncoder.matches(memberPassModifyDTO.getOriginPassword(), member.getPassword());
    }
    return false; // 사용자가 존재하지 않음
  }


  // 관리자용 페이지와 조회
  @Transactional(readOnly = true)
  public Page<MemberDTO> getMemberPage(MemberSearchDTO memberSearchDTO, Pageable pageaple){
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
