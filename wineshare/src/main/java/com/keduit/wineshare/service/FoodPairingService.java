package com.keduit.wineshare.service;

import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.repository.FoodPairingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodPairingService {

  private final FoodPairingRepository foodPairingRepository;

  public List<FoodPairing> getAllFoodPairing(){
    return foodPairingRepository.findAll();
  }

  public FoodPairing getFoodPairingByFood(String food){
    return foodPairingRepository.findByFood(food);
  }
}
