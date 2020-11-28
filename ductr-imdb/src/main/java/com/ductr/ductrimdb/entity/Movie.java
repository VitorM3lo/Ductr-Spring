package com.ductr.ductrimdb.entity;

import java.util.List;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Movie {
  
  private String titleId;
  
  private String title;

  private String region;

  private String language;

  private Type type;

  private int startYear;

  private int endYear;

  private int runtime;

  private List<Genre> genres;

  private double rating;

  private long numberVotes;

  private List<Episode> episodes;

}
