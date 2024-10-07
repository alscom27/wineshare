package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineRepository extends JpaRepository<Wine, Long> {


}
