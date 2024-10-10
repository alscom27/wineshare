package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Cellar;
import com.keduit.wineshare.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CellarRepository extends JpaRepository<Cellar, Long> {

  Cellar findByMemberId(Long memberId); // 해당 멤버의 셀러를 찾아오기

}
