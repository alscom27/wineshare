package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTests {

  @Autowired
  MemberService memberService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원 가입 테스트")
  public void saveMemberTest(){
    Member member = new Member();
    member.setEmail("member1@member.com");
    member.setPassword("12345678");
    member.setName("멤버");
    member.setNickname("나자바");
    member.setRRN("234567-2345678");
    member.setPhoneNumber("010-2222-2222");
    member.setWithdrawStatus(WithdrawStatus.LEAVE);
    member.setMemberType(MemberType.EXPERT);
    Member savedMember = memberService.saveMember(member);
    System.out.println(savedMember);

    assertEquals(member.getEmail(), savedMember.getEmail());
    assertEquals(member.getName(), savedMember.getName());
    assertEquals(member.getPassword(), savedMember.getPassword());
    assertEquals(member.getMemberType(), savedMember.getMemberType());
    assertEquals(member.getWithdrawStatus(), savedMember.getWithdrawStatus());
  }

}
