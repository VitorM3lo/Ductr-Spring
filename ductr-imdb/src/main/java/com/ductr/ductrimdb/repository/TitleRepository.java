package com.ductr.ductrimdb.repository;

import com.ductr.ductrentity.entities.Title;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends PagingAndSortingRepository<Title, String> {

  @Cacheable("titles")
  @Query("select t from Title t join t.alternateTitles tat join t.type ty join t.genres g where (lower(t.originalTitle) like '%' || lower(:title) || '%' or lower(t.primaryTitle) like '%' || lower(:title) || '%' or lower(tat.title) like '%' || lower(:title) || '%') and (lower(ty.type) = lower(:type) or :type is null or :type = '') and (lower(g.genre) = lower(:genre) or :genre is null or :genre = '') and t.rating >= :rating group by t.tconst")
  public Page<Title> findByTitle(String title, String type, String genre, double rating, Pageable pageable);

}
