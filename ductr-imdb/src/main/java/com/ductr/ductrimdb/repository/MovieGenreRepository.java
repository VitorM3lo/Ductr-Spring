package com.ductr.ductrimdb.repository;

import com.ductr.ductrimdb.entity.Genre;

import org.springframework.data.repository.CrudRepository;

public interface MovieGenreRepository extends CrudRepository<Genre, String> {
  
}
