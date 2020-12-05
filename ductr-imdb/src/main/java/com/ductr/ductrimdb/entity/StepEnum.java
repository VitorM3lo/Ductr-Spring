package com.ductr.ductrimdb.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StepEnum {
  
  MOVIE_DATA(1),
  MOVIE_BASICS(2);

  private int step;

  public int getStep() {
    return step;
  }

}
