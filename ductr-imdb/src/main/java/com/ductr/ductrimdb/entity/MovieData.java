package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@IdClass(MovieDataId.class)
public class MovieData {

  @Id
  @NonNull
  private int id;

  @Id
  @NonNull
  private String titleId;

  @NonNull
  @Column(length = 1000)
  private String title;

  @NonNull
  private String region;

  @NonNull
  private String language;

  @OneToOne
  private Type type;

  private int startYear;

  private int endYear;

  private int runtime;

  @ManyToMany
  private List<Genre> genres;

  private double rating;

  private long numberVotes;

  private boolean adult;

}
