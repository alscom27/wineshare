package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.AromaWheel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AromaWheelRepository extends JpaRepository<AromaWheel, Long> {

  AromaWheel findByAroma(String aroma);
}
