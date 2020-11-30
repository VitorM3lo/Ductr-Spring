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
public class Movie {
  
  @Id
  @NonNull
  private String id;

  @NonNull
  @OneToOne
  private MovieData data;

  private Crew crew;

  @OneToMany
  private List<Person> principals;

}
