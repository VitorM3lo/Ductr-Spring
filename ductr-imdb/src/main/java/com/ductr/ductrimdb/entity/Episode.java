package com.ductr.ductrimdb.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Episode {
  
  private Movie movie;

  private int season;

  private int episode;

}
