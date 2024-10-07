package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.UserType;
import com.keduit.wineshare.constant.WithdrawStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(unique = true)
  private String email;

  private String name;

  @Column(unique = true)
  private String nickname;

  private String password;

  private int phoneNumber;

  private int RRN;    // 주민번호

  @Enumerated(EnumType.STRING)
  private UserType userType;

  @Enumerated(EnumType.STRING)
  private WithdrawStatus withdrawStatus;


}
