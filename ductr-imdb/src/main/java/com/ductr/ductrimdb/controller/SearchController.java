package com.ductr.ductrimdb.controller;

import java.util.List;

import com.ductr.ductrimdb.dto.GenreDto;
import com.ductr.ductrimdb.dto.TitleDto;
import com.ductr.ductrimdb.dto.TypeDto;
import com.ductr.ductrimdb.service.ScraperService;
import com.ductr.ductrimdb.service.SearchService;
import com.ductr.ductrimdb.service.UpdaterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

  @Autowired
  SearchService searchService;

  @Autowired
  ScraperService scraperService;

  //TODO: Maybe with an event of fetching the description or poster it executes the updater
  @Autowired
  UpdaterService updaterService;

  @GetMapping("titles")
  public List<TitleDto> getTitles(@RequestParam String search, @RequestParam(required = false) String type,
      @RequestParam(required = false) String genre, @RequestParam(required = false, defaultValue = "0") double rating,
      @RequestParam int page) {
    return searchService.searchTitles(search, type, genre, rating, page);
  }

  @GetMapping("description")
  public String getDescription(@RequestParam String tconst) {
    String description = scraperService.getDescription(tconst);
    new Thread(() -> {
      updaterService.updateTitleDescription(tconst, description);
    }).start();
    return description;
  }

  @GetMapping("poster")
  public String getPoster(@RequestParam String tconst) {
    String link = scraperService.getPosterLink(tconst);
    new Thread(() -> {
      updaterService.updateTitleDescription(tconst, link);
    }).start();
    return link;
  }

  @GetMapping("types")
  public List<TypeDto> getTypes() {
    return searchService.getTypes();
  }

  @GetMapping("genres")
  public List<GenreDto> getGenres() {
    return searchService.getGenres();
  }

}
