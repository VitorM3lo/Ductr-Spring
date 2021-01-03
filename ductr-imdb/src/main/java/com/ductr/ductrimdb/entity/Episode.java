package com.ductr.ductrimdb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.micrometer.core.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Episode implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private EpisodeKey title;

  @NonNull
  @ManyToOne
  private Title titleParent;

  private int season;
  private int episode;

}
