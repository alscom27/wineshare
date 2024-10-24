package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

  Member findByEmail(String email);

  List<Member> findByEmailIn(List<String> emails);

  Member findByNickname(String nickname);
}
