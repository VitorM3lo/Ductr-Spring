package com.ductr.ductr.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public class IMDbRepository {

  @Value("${imdb.endpoint}")
  String imdbEndpoint;

  public List<String> getMovie() {
    return null;
  }
}
