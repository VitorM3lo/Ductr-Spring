package com.ductr.ductrimdb.processor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrentity.entities.TitleRegion;
import com.ductr.ductrimdb.repository.TitleRegionRepository;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class TitleRegionProcessor implements ItemProcessor<TitleRegion, Title> {

  @Value("${regions}")
  private String[] regions;

  @Autowired
  private TitleRepository repository;

  @Autowired
  private TitleRegionRepository regionRepository;

  @Override
  public Title process(TitleRegion item) throws Exception {
    if (item == null) {
      return null;
    }
    if (Arrays.asList(regions).contains(item.getRegion())) {
      Optional<Title> fetchedTitle = repository.findById(item.getTconst());
      if (fetchedTitle.isPresent()) {
        Title title = fetchedTitle.get();
        if (title.getAlternateTitles() == null) {
          title.setAlternateTitles(new HashSet<>());
        }
        if (!title.getAlternateTitles().contains(item)) {
          TitleRegion region = regionRepository.save(item);
          title.getAlternateTitles().add(region);
        }
        return title;
      }
    }
    return null;
  }

}
