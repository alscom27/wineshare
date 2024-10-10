package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineDevelopRepository extends JpaRepository<WineDevelop, Long> {

  List<WineDevelop> findByWine(Wine wine);
}
