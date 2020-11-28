package com.ductr.ductrmain.repository;

import java.util.List;

import com.ductr.ductrmain.entity.SubtitleData;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubtitleDataRepository extends CrudRepository<SubtitleData, Integer> {

	@Query("select sd from SubtitleData sd join sd.movieIds mh where sd.languageId = :languageCode and mh = :id")
	List<SubtitleData> getByMovieHashAndLanguageCode(int id, String languageCode);

	@Query("select sd from SubtitleData sd where sd.id = :subtitleId")
	SubtitleData findById(String subtitleId);

}
