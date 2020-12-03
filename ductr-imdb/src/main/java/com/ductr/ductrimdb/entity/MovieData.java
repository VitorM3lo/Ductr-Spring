package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Required;

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
