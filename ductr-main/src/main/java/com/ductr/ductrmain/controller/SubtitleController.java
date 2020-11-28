package com.ductr.ductrmain.controller;

import java.util.Arrays;
import java.util.List;

import com.ductr.ductrmain.dto.SubtitleDataDto;
import com.ductr.ductrmain.service.SubtitleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubtitleController {

  @Autowired
  SubtitleService subtitleService;

  @CrossOrigin
  @GetMapping("/subtitles")
  ResponseEntity<List<SubtitleDataDto>> getSubtitles(@RequestParam int id,
      @RequestParam(required = false) String lang) {
    List<SubtitleDataDto> result = this.subtitleService.getSubtitles(id, lang);
    return result != null ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @CrossOrigin
  @RequestMapping(value = "/subtitle", method = RequestMethod.GET, produces = "text/plain")
  Resource getSubtitle(@RequestParam String subtitleId, @RequestParam int movieId) {
    return this.subtitleService.getSubtitleResource(subtitleId, movieId);
  }

  @CrossOrigin
  @GetMapping("/languages")
  List<String> getLanguages() {
    String[] languages = { "pob", "por", "eng" };
    return Arrays.asList(languages);
  }

}
