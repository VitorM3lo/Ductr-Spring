package com.ductr.ductr.repository;

import com.ductr.ductr.entity.Subtitle;
import com.ductr.ductr.entity.SubtitleData;

import org.springframework.data.repository.CrudRepository;

public interface SubtitleRepository extends CrudRepository<Subtitle, Integer> {

	Subtitle findBySubtitleData(SubtitleData subtitleData);
  
}
