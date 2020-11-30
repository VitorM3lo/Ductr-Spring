package com.ductr.ductrimdb.dto;

import java.util.List;

import lombok.Data;

@Data
public class CrewDto {
  
  String titleId;

  int ordering;

  String title;

  String region;

  String language;

  List<String> types;

  List<String> attributes;

  boolean isOriginalTitle;

}
