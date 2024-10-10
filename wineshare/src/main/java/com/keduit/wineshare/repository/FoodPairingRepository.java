package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.FoodPairing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FoodPairingRepository extends JpaRepository<FoodPairing, Long> {

  @Query("SELECT f.foodImg FROM FoodPairing f WHERE f.food = ?1")
  String findFoodImgByFood(String food);


}
