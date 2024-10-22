package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Marketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MarketingRepository  extends JpaRepository<Marketing, Long>, QuerydslPredicateExecutor<Marketing>, MarketingRepositoryCustom {


}
