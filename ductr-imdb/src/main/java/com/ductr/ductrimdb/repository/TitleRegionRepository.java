package com.ductr.ductrimdb.repository;

import java.util.Set;

import com.ductr.ductrentity.entities.TitleRegion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRegionRepository extends JpaRepository<TitleRegion, Integer> {

  @Query("select tr from TitleRegion tr where tr.title like '%' || :title || '%'")
  public Set<TitleRegion> findByTitle(String title);

}
