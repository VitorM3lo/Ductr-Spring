package com.ductr.ductrimdb.controller;

import java.util.List;

import com.ductr.ductrimdb.dto.TitleDto;
import com.ductr.ductrimdb.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

  @Autowired
  SearchService searchService;

  @GetMapping("titles")
  public List<TitleDto> getTitles(@RequestParam String search, @RequestParam(required = false) String type,
      @RequestParam(required = false) String genre, @RequestParam(required = false, defaultValue = "0") double rating,
      @RequestParam int page) {
    return searchService.searchTitles(search, type, genre, rating, page);
  }

}
