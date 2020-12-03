package com.ductr.ductrimdb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.ductr.ductrimdb.dto.MovieBasicsDto;
import com.ductr.ductrimdb.dto.MovieDataDto;
import com.ductr.ductrimdb.entity.MovieData;
import com.ductr.ductrimdb.entity.Type;
import com.ductr.ductrimdb.mapper.MovieDataMapper;
import com.ductr.ductrimdb.repository.IMDbFileRepository;
import com.ductr.ductrimdb.repository.MovieDataRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class ImdbFileIndexer {

  @Autowired
  private MovieDataRepository movieDataRepository;

  @Autowired
  private IMDbFileRepository imdbFileRepository;

  @Autowired
  private MovieDataMapper movieDataMapper;

  @Value("${regions}")
  List<String> regions;

  @Scheduled(cron = "0 1 * * * SUN")
  public void index() {
    this.indexMovieData();
    // this.indexMoviesDataBasics();
  }

  private void indexMovieData() {
    File file = this.imdbFileRepository.getMovieDataFile();
    try {
      CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
      CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).withSkipLines(1).build();
      String[] line;
      while ((line = csvReader.readNext()) != null) {
        final MovieDataDto dto = movieDataMapper.mapToMovieDataDto(line);
        if (regions.contains(dto.getRegion())) {
          this.movieDataRepository.save(new MovieData(dto.getOrdering(), dto.getTitleId(), dto.getTitle(), dto.getRegion()));
        }
      }
    } catch (IllegalStateException | FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void indexMoviesDataBasics() {
    File file = this.imdbFileRepository.getMovieDataBasicsFile();
    try {
      List<MovieBasicsDto> beans = new CsvToBeanBuilder<MovieBasicsDto>(new FileReader(file))
          .withType(MovieBasicsDto.class).build().parse();
      for (MovieBasicsDto dto : beans) {
        Optional<MovieData> data = this.movieDataRepository.findById(dto.getTconst());
        if (data.isPresent() && data.get() != null) {
          MovieData movieData = data.get();
          movieData.setAdult(dto.isAdult());
          movieData.setStartYear(dto.getStartYear());
          movieData.setEndYear(dto.getEndYear());
          movieData.setRuntime(dto.getRuntimeMinutes());
          movieData.setType(new Type(dto.getTitleType()));
          this.movieDataRepository.save(movieData);
        }
      }
    } catch (IllegalStateException | FileNotFoundException e) {
      e.printStackTrace();
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
