package com.ductr.ductrimdb.repository;

import com.ductr.ductrimdb.entity.Movie;

import org.springframework.data.repository.CrudRepository;

public interface MovieDataRepository extends CrudRepository<Movie, Integer> {

}
