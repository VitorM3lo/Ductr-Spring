package com.ductr.ductrimdb.repository;

import com.ductr.ductrimdb.entity.Type;

import org.springframework.data.repository.CrudRepository;

public interface MovieTypeRepository extends CrudRepository<Type, String> {

}