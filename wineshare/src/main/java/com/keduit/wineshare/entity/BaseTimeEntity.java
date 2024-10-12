package com.keduit.wineshare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
  @Column(updatable = false)
  private LocalDateTime regTime;

  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
  private LocalDateTime updateTime;

}
