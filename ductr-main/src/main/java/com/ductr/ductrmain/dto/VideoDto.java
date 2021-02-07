package com.ductr.ductrmain.dto;

import com.ductr.ductrmain.entity.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class VideoDto {

  @JsonProperty("id")
  private int id;
  @NonNull
  @JsonProperty("name")
  private String name;
  @JsonProperty("imdbId")
  private String imdbId;
  @JsonProperty("status")
  private StatusEnum status;

}
