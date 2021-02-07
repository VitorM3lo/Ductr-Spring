package com.ductr.ductrimdb.repository;

import com.ductr.ductrentity.entities.Type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, String>{
  
}
