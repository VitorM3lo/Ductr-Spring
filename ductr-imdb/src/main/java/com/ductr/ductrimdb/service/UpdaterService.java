package com.ductr.ductrimdb.service;

import java.util.Optional;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdaterService {

  @Autowired
  private TitleRepository titleRepository;

  public void updateTitleDescription(String tconst, String description) {
    Optional<Title> title = titleRepository.findById(tconst);
    if (title.isPresent()) {
      Title titleObj = title.get();
      titleObj.setDescription(description);
      titleRepository.save(titleObj);
    }
  }

  public void updateTitlePoster(String tconst, String link) {
    Optional<Title> title = titleRepository.findById(tconst);
    if (title.isPresent()) {
      Title titleObj = title.get();
      titleObj.setPosterLink(link);
      titleRepository.save(titleObj);
    }
  }

}
