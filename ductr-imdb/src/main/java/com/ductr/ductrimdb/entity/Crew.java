package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Crew {
  
  private List<Person> directors;

  private List<Person> writers;

}
