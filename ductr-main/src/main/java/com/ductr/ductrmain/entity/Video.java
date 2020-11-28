package com.ductr.ductrmain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Video implements BaseFile {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;

  @NonNull
  private String name;

  @NonNull
  private String path;

  private String imdbId;

  @NonNull
  private StatusEnum status;

  @NonNull
  private String hash;

  @OneToOne
  private Subtitle subtitle;

}
