package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.AromaWheel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AromaWheelRepository extends JpaRepository<AromaWheel, Long> {

  @Query("SELECT a.aromaValue FROM AromaWheel a WHERE a.aroma = ?1")
  String findAromaValueByAroma(String aroma);
}
