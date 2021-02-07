package com.ductr.ductrimdb.repository;

import java.util.Set;

import com.ductr.ductrentity.entities.Title;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Cacheable
public interface TitleRepository extends JpaRepository<Title, String> {

  @Query("select t from Title t where t.originalTitle like :title or t.primaryTitle like :title")
  public Set<Title> findByTitle(String title);

}
