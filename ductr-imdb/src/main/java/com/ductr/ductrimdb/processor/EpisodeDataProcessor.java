package com.ductr.ductrimdb.processor;

import java.util.Optional;

import com.ductr.ductrimdb.entity.Episode;
import com.ductr.ductrimdb.entity.EpisodeData;
import com.ductr.ductrimdb.entity.EpisodeKey;
import com.ductr.ductrimdb.entity.Title;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EpisodeDataProcessor implements ItemProcessor<EpisodeData, Episode> {

  @Autowired
  TitleRepository repository;

  @Override
  public Episode process(EpisodeData item) throws Exception {
    Optional<Title> fetchedParentTitle = repository.findById(item.getParentTconst());
    Optional<Title> fetchedTitle = repository.findById(item.getTconst());
    if (fetchedParentTitle.isPresent() && fetchedTitle.isPresent()) {
      log.info("parent: " + item.getParentTconst() + " - tconst: " + item.getTconst());
      Episode episode = new Episode();
      EpisodeKey key = new EpisodeKey();
      key.setTitle(fetchedTitle.get());
      episode.setTitle(key);
      episode.setTitleParent(fetchedParentTitle.get());
      episode.setEpisode(item.getEpisodeNumber());
      episode.setSeason(item.getSeasonNumber());
      return episode;
    }
    return null;
  }

}
