package com.ductr.ductrentity.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Episode implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @NonNull
  private EpisodeKey title;

  @ManyToOne
  @NonNull
  private Title titleParent;

  private int season;
  private int episode;

}
