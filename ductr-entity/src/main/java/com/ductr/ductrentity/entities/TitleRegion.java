package com.ductr.ductrentity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class TitleRegion {
  
  @Id
  @EqualsAndHashCode.Exclude
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Transient
  @NonNull
  private String tconst;

  @Column(length = 1000)
  @NonNull
  private String title;

  private String region;

  private boolean original;

}
