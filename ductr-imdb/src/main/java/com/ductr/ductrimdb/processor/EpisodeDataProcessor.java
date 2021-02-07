package com.ductr.ductrimdb.processor;

import java.util.Optional;

import com.ductr.ductrentity.entities.Episode;
import com.ductr.ductrentity.entities.EpisodeKey;
import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.entity.EpisodeData;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class EpisodeDataProcessor implements ItemProcessor<EpisodeData, Episode> {

  @Autowired
  TitleRepository repository;

  @Override
  public Episode process(EpisodeData item) throws Exception {
    Optional<Title> fetchedParentTitle = repository.findById(item.getParentTconst());
    Optional<Title> fetchedTitle = repository.findById(item.getTconst());
    if (fetchedParentTitle.isPresent() && fetchedTitle.isPresent()) {
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
