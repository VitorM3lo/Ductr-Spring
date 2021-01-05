package com.ductr.ductrentity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Title implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @NonNull
  private String tconst;

  @NonNull
  @Embedded
  private Type titleType;

  @NonNull
  @Column(length = 1000)
  private String primaryTitle;

  @NonNull
  @Column(length = 1000)
  private String originalTitle;

  private boolean isAdult;

  private int startYear;

  private int endYear;

  private int runtimeMinutes;

  private double rating;

  @ElementCollection
  private List<Genre> genres;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<Person> directors;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<Person> writers;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<Person> principals;

  @ElementCollection
  private List<TitleRegion> alternateTitles;

}
