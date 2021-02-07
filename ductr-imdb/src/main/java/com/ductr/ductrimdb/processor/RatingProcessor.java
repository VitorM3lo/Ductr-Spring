package com.ductr.ductrimdb.processor;

import java.util.Optional;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.repository.TitleRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class RatingProcessor implements ItemProcessor<Title, Title> {

  @Autowired
  TitleRepository repository;

  @Override
  public Title process(Title item) throws Exception {
    Optional<Title> fetchedOptionalTitle = repository.findById(item.getTconst());
    if (fetchedOptionalTitle.isPresent()) {
      Title fetchedTitle = fetchedOptionalTitle.get();
      fetchedTitle.setRating(item.getRating());
      return fetchedTitle;
    }
    return null;
  }
  
}
