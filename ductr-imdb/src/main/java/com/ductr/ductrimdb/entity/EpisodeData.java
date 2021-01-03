package com.ductr.ductrimdb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeData {

  private String tconst;
  private String parentTconst;
  private int episodeNumber;
  private int seasonNumber;

}
