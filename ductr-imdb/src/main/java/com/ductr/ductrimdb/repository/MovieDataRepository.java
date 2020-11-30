package com.ductr.ductrimdb.repository;

import com.ductr.ductrimdb.entity.MovieData;

import org.springframework.data.repository.CrudRepository;

public interface MovieDataRepository extends CrudRepository<MovieData, String> {

}
