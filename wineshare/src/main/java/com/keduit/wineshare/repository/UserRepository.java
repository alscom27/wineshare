package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

}
