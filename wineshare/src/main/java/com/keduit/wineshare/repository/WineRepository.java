package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface WineRepository extends JpaRepository<Wine, Long> {


  Wine findByWineName(String wineName);
}
