package com.ductr.ductrimdb.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Person {
  
  private String id;

  private String name;

}
