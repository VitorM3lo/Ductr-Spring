package com.ductr.ductrimdb.dto;

import java.util.List;

import lombok.Data;

@Data
public class TitleDto {

  private String tconst;
  private String title;
  private double rating;
  private int startYear;
  private int endYear;
  private String type;
  private List<String> genres;
  private List<String> alternateTitles;

  // TODO: ADD Directors and Actors

}
