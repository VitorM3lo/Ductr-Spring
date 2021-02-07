package com.ductr.ductrimdb.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ductr.ductrentity.entities.Genre;
import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrentity.entities.Type;
import com.ductr.ductrimdb.dto.TitleDto;
import com.ductr.ductrimdb.repository.GenreRepository;
import com.ductr.ductrimdb.repository.TitleRegionRepository;
import com.ductr.ductrimdb.repository.TitleRepository;
import com.ductr.ductrimdb.repository.TypeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

  @Autowired
  private TitleRepository repository;

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private TypeRepository typeRepository;

  @Autowired
  private TitleRegionRepository regionRepository;

  @Autowired
  ModelMapper modelMapper;

  public List<TitleDto> searchTitles(String search, String type, String genre, int page) {
    PageRequest pageRequest = PageRequest.of(page - 1, 5);
    // Page<Title> pageTitle = repository.getTitles(search, pageRequest);
    // if (pageTitle.hasContent()) {
    //   List<Title> titles = pageTitle.getContent();
    //   List<TitleDto> dtos = new ArrayList<>();
    //   for (Title title : titles) {
    //     TitleDto dto = modelMapper.map(title, TitleDto.class);
    //     dto.setGenres(title.getGenres().stream().map(g -> g.getGenre()).collect(Collectors.toList()));
    //     dto.setType(title.getType().getType());
    //     dto.setTitle(title.getPrimaryTitle());
    //     dtos.add(dto);
    //   }
    //   return dtos;
    // }
    return null;
  }

  // private Set<TitleRegion> getAlternateTitles(String title) {
  //   Set<TitleRegion> regions = this.regionRepository.findByTitle(title);
  //   Set<TitleRegion> allTitles = new HashSet<>();
  //   for (TitleRegion titleRegion : regions) {
  //     allTitles.addAll(this.regionRepository.findByTconst(titleRegion.getTconst()));
  //   }
  //   return allTitles;
  // }

  private Set<Title> getTitles(String title, String type, String genre, double rating) {
    Set<Title> titles = this.repository.findByTitle(title);
    Set<Title> filteredTitles = new HashSet<>();
    for (Title titleObj : titles) {
      if (titleObj.getGenres().contains(new Genre(genre)) && titleObj.getType().equals(new Type(type))
          && titleObj.getRating() >= rating) {
        filteredTitles.add(titleObj);
      }
    }
    return filteredTitles;
  }

  private Set<TitleDto> getTitleData(String title, String type, String genre, double rating) {
    Set<Title> titles = getTitles(title, type, genre, rating);
    // Set<TitleRegion> regions = getAlternateTitles(title);
    return null;
  }

}
