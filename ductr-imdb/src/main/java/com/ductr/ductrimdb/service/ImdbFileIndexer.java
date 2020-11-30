package com.ductr.ductrimdb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import com.ductr.ductrimdb.dto.MovieBasicsDto;
import com.ductr.ductrimdb.dto.MovieDataDto;
import com.ductr.ductrimdb.entity.MovieData;
import com.ductr.ductrimdb.entity.Type;
import com.ductr.ductrimdb.repository.IMDbFileRepository;
import com.ductr.ductrimdb.repository.MovieDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ImdbFileIndexer {

  @Autowired
  MovieDataRepository movieDataRepository;

  @Autowired
  IMDbFileRepository imdbFileRepository;

  // TODO: Parse files and create data, at each one insert in database
  // Additional data must query the base and then append and save again - update
  @Scheduled(cron = "0 1 * * * SUN")
  public void index() {
    this.indexMovieData();
    this.indexMoviesDataBasics();
  }

  private void indexMovieData() {
    File file = this.imdbFileRepository.getMovieDataFile(null);
    try {
      List<MovieDataDto> beans = new CsvToBeanBuilder<MovieDataDto>(new FileReader(file)).withType(MovieDataDto.class)
          .build().parse();
      for (MovieDataDto movieDataDto : beans) {
        this.movieDataRepository
            .save(new MovieData(movieDataDto.getTitleId(), movieDataDto.getTitle(), movieDataDto.getLanguage()));
      }
    } catch (IllegalStateException | FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private List<MovieData> indexMoviesDataBasics() {
    File file = this.imdbFileRepository.getMovieDataBasicsFile(null);
    try {
      List<MovieBasicsDto> beans = new CsvToBeanBuilder<MovieBasicsDto>(new FileReader(file))
          .withType(MovieBasicsDto.class).build().parse();
      for (MovieBasicsDto dto : beans) {
        MovieData data = this.movieDataRepository.findById(dto.getTconst()).get();
        if (data != null) {
          data.setAdult(dto.isAdult());
          data.setStartYear(dto.getStartYear());
          data.setEndYear(dto.getEndYear());
          data.setRuntime(dto.getRuntimeMinutes());
          data.setType(new Type(dto.getTitleType()));
        }
        this.movieDataRepository.save(data);
      }
      return null;
    } catch (IllegalStateException | FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void indexRatings() {
    throw new NotImplementedException();
  }

  private void indexSeries() {
    throw new NotImplementedException();
  }

  private void indexPersons() {
    throw new NotImplementedException();
  }

  private void indexCrew() {
    throw new NotImplementedException();
  }

  private void indexPrincipals() {
    throw new NotImplementedException();
  }

}
