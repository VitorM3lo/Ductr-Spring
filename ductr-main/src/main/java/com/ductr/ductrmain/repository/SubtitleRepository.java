package com.ductr.ductrmain.repository;

import com.ductr.ductrmain.entity.Subtitle;
import com.ductr.ductrmain.entity.SubtitleData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtitleRepository extends JpaRepository<Subtitle, Integer> {

	Subtitle findBySubtitleData(SubtitleData subtitleData);

}
