package com.ductr.ductrentity.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Embeddable
public class EpisodeKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @OneToOne
  @JoinColumn(name = "tconst")
  private Title title;
  
}
