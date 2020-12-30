package com.ductr.ductrimdb.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StepEnum {
  
  TITLE_DATA(1),
  RATINGS_DATA(2),
  PERSON_DATA(3),
  CREW_DATA(4),
  PRINCIPALS_DATA(5),
  SERIES_DATA(6),
  TITLE_AKA_DATA(7);

  private int step;

  public int getStep() {
    return step;
  }

}
