package com.keduit.wineshare;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.keduit.wineshare.entity.Wine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class WineshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WineshareApplication.class, args);

	}


}
