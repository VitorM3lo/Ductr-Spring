package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Data;

@Embeddable
@Data
public class Crew {

  @OneToMany
  private List<Person> directors;

  @OneToMany
  private List<Person> writers;

}
