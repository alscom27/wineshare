package com.keduit.wineshare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keduit.wineshare.constant.WineType;
import com.keduit.wineshare.dto.WineDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Wine extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wine_id")
  private Long id;

  //  @Column(unique = true) // 와인이름 중복될 수 없게
  private String wineName;

  @Enumerated(EnumType.STRING)
  private WineType wineType;

  private String country;

  private String region;

  private String wineImg;

  private int price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;


  // WineDevelop과의 관계 설정
  @JsonIgnore
  @OneToMany(mappedBy = "wine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<WineDevelop> wineDevelops; // WineDevelop 리스트




  public static Wine createWine(WineDTO wineDTO, Member member) {
    Wine wine = new Wine();

    wine.setWineName(wineDTO.getWineName());
    wine.setWineType(wineDTO.getWineType());
    wine.setCountry(wineDTO.getCountry());
    wine.setRegion(wineDTO.getRegion());
    wine.setWineImg(wineDTO.getWineImg());
    wine.setPrice(wineDTO.getPrice());
    wine.setMember(member);

    return wine;
  }

}

