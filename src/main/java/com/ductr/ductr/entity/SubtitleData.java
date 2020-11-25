package com.ductr.ductr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class SubtitleData {

  @Id
  @NonNull
  private String id;

  @NonNull
  private String filename;

  @NonNull
  @Column(name = "release_name") // he gets confused with release command
  private String release;

  @NonNull
  private String languageCode;

  @NonNull
  private String languageId;

  @NonNull
  private String url;

  private int quantity;

  @ElementCollection(targetClass = Integer.class)
  private List<Integer> movieIds;

  public boolean addMovieId(int id) {
    if (this.movieIds == null) {
      this.movieIds = new ArrayList<>();
    }
    return this.movieIds.add(id);
  }

  public boolean removeMovieId(int id) {
    if (this.movieIds == null) {
      this.movieIds = new ArrayList<>();
    }
    return this.movieIds.remove(Integer.valueOf(id));
  }

}
