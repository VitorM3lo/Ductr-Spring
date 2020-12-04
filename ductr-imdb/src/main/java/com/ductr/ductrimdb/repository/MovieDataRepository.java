package com.ductr.ductrimdb.repository;

import java.util.List;
import java.util.Optional;

import com.ductr.ductrimdb.entity.MovieData;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MovieDataRepository extends CrudRepository<MovieData, String> {

  @Query("select mv from MovieData mv where mv.titleId = :titleId")
  Optional<List<MovieData>> findByTitleId(String titleId);

}
