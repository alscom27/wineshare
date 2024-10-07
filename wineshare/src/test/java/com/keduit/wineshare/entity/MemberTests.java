package com.keduit.wineshare.entity;

import com.keduit.wineshare.WineshareApplication;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;


@SpringBootTest //어플리케이션과 같은위치거나 하위폴더에 있어야 테스트 가능
@Transactional
public class MemberTests {

  @Autowired
  MemberRepository memberRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("Auditing 테스트")
  @WithMockUser(username = "tester01", roles = "USER")
  public void auditingTest(){
    Member newMember = new Member();
    memberRepository.save(newMember);

    em.flush();
    em.clear();
    Member member = memberRepository.findById(newMember.getId())
        .orElseThrow(EntityNotFoundException::new);

    System.out.println("register time : " + member.getRegTime());
    System.out.println("update time : " + member.getUpdateTime());
    System.out.println("create member : " + member.getRegBy());
    System.out.println("modified member : " + member.getUpdateBy());
  }

}
