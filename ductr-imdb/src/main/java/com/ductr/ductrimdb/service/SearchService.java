package com.ductr.ductrimdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.dto.GenreDto;
import com.ductr.ductrimdb.dto.TitleDto;
import com.ductr.ductrimdb.dto.TypeDto;
import com.ductr.ductrimdb.repository.GenreRepository;
import com.ductr.ductrimdb.repository.TitleRepository;
import com.ductr.ductrimdb.repository.TypeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
  private ModelMapper modelMapper;

  public List<TitleDto> searchTitles(String title, String type, String genre, double rating, int page) {
    PageRequest pageRequest = PageRequest.of(page - 1, 5);
    Page<Title> pageTitle = this.repository.findByTitle(title, type, genre, rating, pageRequest);
    if (pageTitle.hasContent()) {
      List<Title> titles = pageTitle.getContent();
      List<TitleDto> dtos = new ArrayList<>();
      for (Title titleObj : titles) {
        TitleDto dto = modelMapper.map(titleObj, TitleDto.class);
        dto.setAlternateTitles(
            titleObj.getAlternateTitles().stream().map(tr -> tr.getTitle()).distinct().collect(Collectors.toList()));
        dto.setGenres(titleObj.getGenres().stream().map(g -> g.getGenre()).collect(Collectors.toList()));
        dto.setType(titleObj.getType().getType());
        dto.setTitle(titleObj.getPrimaryTitle());
        dtos.add(dto);
      }
      return dtos;
    }
    return null;
  }

  public List<TypeDto> getTypes() {
    return typeRepository.findAll().stream().map(t -> modelMapper.map(t, TypeDto.class)).collect(Collectors.toList());
  }

  public List<GenreDto> getGenres() {
    return genreRepository.findAll().stream().map(g -> modelMapper.map(g, GenreDto.class)).collect(Collectors.toList());
  }

}
