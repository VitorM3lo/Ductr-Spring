package com.ductr.ductrimdb.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ductr.ductrentity.entities.Genre;
import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrentity.entities.Type;
import com.ductr.ductrimdb.repository.GenreRepository;
import com.ductr.ductrimdb.repository.TypeRepository;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class TitleProcessor implements ItemProcessor<Title, Title> {

  @Autowired
  TypeRepository typeRepository;

  @Autowired
  GenreRepository genreRepository;

  @Value("${minYear}")
  private int minYear;

  @Value("${types}")
  private String[] types;

  @Override
  public Title process(Title item) throws Exception {
    boolean whitelistedType = false;
    if (item == null || item.getStartYear() < minYear) {
      return null;
    }
    for (String string : types) {
      if (item.getType().getType().toLowerCase().contains(string)) {
        whitelistedType = true;
        break;
      }
    }
    if (whitelistedType) {
      Optional<Type> fetchedType = typeRepository.findById(item.getType().getType());
      if (fetchedType.isPresent()) {
        item.setType(fetchedType.get());
      } else {
        item.setType(typeRepository.save(item.getType()));
      }
      Set<Genre> genres = item.getGenres();
      List<Genre> managedGenres = new ArrayList<>();
      for (Genre genre : genres) {
        if (genre == null) {
          continue;
        }
        String genreId = genre.getGenre();
        Optional<Genre> fetchedGenre = genreRepository.findById(genreId);
        if (fetchedGenre != null && fetchedGenre.isPresent()) {
          managedGenres.add(fetchedGenre.get());
        } else {
          managedGenres.add(genreRepository.save(genre));
        }
      }
      item.setGenres(new HashSet<>(managedGenres));
      return item;
    }
    return null;
  }

}
