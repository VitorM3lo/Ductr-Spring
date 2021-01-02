package com.ductr.ductrimdb.processor;

import java.util.ArrayList;
import java.util.Optional;

import com.ductr.ductrimdb.entity.Title;
import com.ductr.ductrimdb.entity.TitleRegion;
import com.ductr.ductrimdb.entity.TitleRegionData;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class TitleRegionProcessor implements ItemProcessor<TitleRegionData, Title> {

  @Autowired
  TitleRepository repository;

  @Override
  public Title process(TitleRegionData item) throws Exception {
    if (item == null) {
      return null;
    }
    Optional<Title> fetchedTitle = repository.findById(item.getTconst());
    if (fetchedTitle.isPresent()) {
      Title title = fetchedTitle.get();
      if (title.getAlternateTitles() == null) {
        title.setAlternateTitles(new ArrayList<>());
      }
      TitleRegion titleRegion = new TitleRegion(item.getTitle(), item.getRegion(), item.isOriginal());
      if (!title.getAlternateTitles().contains(titleRegion)) {
        title.getAlternateTitles().add(titleRegion);
      }
      return title;
    }
    return null;
  }

}
