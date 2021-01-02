package com.ductr.ductrimdb.repository;

import com.ductr.ductrimdb.entity.Title;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {
  
}
