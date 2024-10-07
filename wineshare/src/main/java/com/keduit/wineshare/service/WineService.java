package com.keduit.wineshare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class WineService {

  private final ObjectMapper objectMapper;

  private final WineRepository wineRepository;







}
