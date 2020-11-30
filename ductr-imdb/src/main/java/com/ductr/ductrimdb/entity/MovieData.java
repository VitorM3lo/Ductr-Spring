package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class MovieData {

  @Id
  @NonNull
  private String id;

  @NonNull
  private String title;

  private String region;

  @NonNull
  private String language;

  @OneToOne
  private Type type;

  private int startYear;

  private int endYear;

  private int runtime;

  @OneToMany
  private List<Genre> genres;

  private double rating;

  private long numberVotes;

  private boolean adult;

}
