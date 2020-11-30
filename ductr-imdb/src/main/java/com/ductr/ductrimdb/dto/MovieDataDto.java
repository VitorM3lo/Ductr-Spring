package com.ductr.ductrimdb.dto;

import java.util.List;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class MovieDataDto {
  
  @CsvBindByName
  String titleId;

  @CsvBindByName
  int ordering;

  @CsvBindByName
  String title;

  @CsvBindByName
  String region;

  @CsvBindByName
  String language;

  @CsvBindByName
  List<String> types;

  @CsvBindByName
  List<String> attributes;

  @CsvBindByName
  boolean isOriginalTitle;

}
