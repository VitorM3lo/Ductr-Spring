package com.ductr.ductr.dto;

import com.ductr.ductr.entity.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class VideoDto implements BaseDto {

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
