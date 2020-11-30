package com.ductr.ductrimdb.dto;

import java.util.List;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class MovieBasicsDto {

  @CsvBindByName
  String tconst;
  
  @CsvBindByName
  String titleType;
  
  @CsvBindByName
  String primaryTitle;

  @CsvBindByName
  String originalTitle;

  @CsvBindByName
  boolean isAdult;

  @CsvBindByName
  Integer startYear;

  @CsvBindByName
  Integer endYear;

  @CsvBindByName
  int runtimeMinutes;

  @CsvBindByName
  List<String> genres;

}
