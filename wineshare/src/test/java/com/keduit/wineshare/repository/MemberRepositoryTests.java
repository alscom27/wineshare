package com.keduit.wineshare.repository;

import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberRepositoryTests {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원 가입 테스트")
  public void createMemberTest(){
    MemberDTO dto = new MemberDTO();
    dto.setEmail("test@test.com");
    dto.setName("테스터");
    dto.setNickname("한정교");
    dto.setPassword("12345678");
    dto.setPhoneNumber("010-1111-1111");
    dto.setRRN("123456-1234567");
    Member member = Member.createMember(dto, passwordEncoder);
    memberRepository.save(member);
  }

}
