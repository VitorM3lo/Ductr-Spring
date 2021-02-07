package com.ductr.ductrentity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
  @ManyToOne
  private Type type;

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

  @ManyToMany
  private Set<Genre> genres;

  @OneToMany
  private Set<TitleRegion> alternateTitles;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Person> directors;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Person> writers;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Person> principals;

}
