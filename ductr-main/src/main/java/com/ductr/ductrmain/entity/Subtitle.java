package com.ductr.ductrmain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Subtitle implements BaseFile {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;

  @NonNull
  private String name;

  @NonNull
  private String path;

  @JoinColumn(unique = true)
  @OneToOne
  private SubtitleData subtitleData;

}
