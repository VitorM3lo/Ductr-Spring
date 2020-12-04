package com.ductr.ductrimdb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexState {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private int step;
  private int line;

}
