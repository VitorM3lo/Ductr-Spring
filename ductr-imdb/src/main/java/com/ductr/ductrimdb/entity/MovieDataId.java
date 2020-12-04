package com.ductr.ductrimdb.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class MovieDataId implements Serializable {

  private static final long serialVersionUID = 7815252742940203211L;

  @NonNull
  private String titleId;

  private int id;

}
