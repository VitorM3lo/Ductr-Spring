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
public class Series {
  
  @Id
  @NonNull
  private String id;

  @NonNull
  @OneToOne
  private MovieData data;

  private int season;

  private int episode;

  private Crew crew;

  @OneToMany
  private List<Person> principals;

}
