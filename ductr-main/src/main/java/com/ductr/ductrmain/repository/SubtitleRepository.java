package com.ductr.ductrmain.repository;

import com.ductr.ductrmain.entity.Subtitle;
import com.ductr.ductrmain.entity.SubtitleData;

import org.springframework.data.repository.CrudRepository;

public interface SubtitleRepository extends CrudRepository<Subtitle, Integer> {

	Subtitle findBySubtitleData(SubtitleData subtitleData);

}
