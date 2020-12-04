package com.ductr.ductrimdb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PreDestroy;

import com.ductr.ductrimdb.dto.MovieBasicsDto;
import com.ductr.ductrimdb.dto.MovieDataDto;
import com.ductr.ductrimdb.entity.IndexState;
import com.ductr.ductrimdb.entity.MovieData;
import com.ductr.ductrimdb.entity.Type;
import com.ductr.ductrimdb.mapper.MovieDataMapper;
import com.ductr.ductrimdb.repository.IMDbFileRepository;
import com.ductr.ductrimdb.repository.MovieDataRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImdbFileIndexer {

  @Autowired
  private MovieDataRepository movieDataRepository;

  @Autowired
  private IMDbFileRepository imdbFileRepository;

  @Autowired
  private IndexStateService indexStateService;

  @Autowired
  private MovieDataMapper movieDataMapper;

  @Value("${regions}")
  List<String> regions;

  private IndexState state = null;

  @Scheduled(cron = "0 1 * * * SUN")
  public void index() {
    this.state = this.indexStateService.getIndexState();
    if (this.state != null) {
      indexStepped(this.state);
    } else {
      indexSequentially();
    }
  }
  
  @PreDestroy
  private void shutdown() {
    this.indexStateService.setIndexState(this.state);
  }

  private void indexStepped(IndexState state) {
    switch (state.getStep()) {
      case 1:
      this.indexMovieData(state.getLine());
      break;
      case 2:
      this.indexMoviesDataBasics(state.getLine());
      break;
      default:
      break;
    }
  }
  
  private void indexSequentially() {
    this.indexMovieData(-1);
    this.indexMoviesDataBasics(-1);
  }

  private void indexMovieData(int skipLines) {
    MovieDataDto dto = null;

    File file = this.imdbFileRepository.getMovieDataFile();

    try {
      CSVReader csvReader = buildReader(file);

      if (skipLines != -1) {
        // exclude header
        csvReader.skip(skipLines - 1);
      } else {
        this.state = new IndexState(1, 1, csvReader.getSkipLines());
      }
      
      String[] line;
      log.info("Started movie data indexing...");
      while ((line = csvReader.readNext()) != null) {
        dto = movieDataMapper.mapToMovieDataDto(line);
        this.state.setLine(this.state.getLine() + 1);
        if (regions.contains(dto.getRegion())) {
          this.movieDataRepository.save(
              new MovieData(dto.getOrdering(), dto.getTitleId(), dto.getTitle(), dto.getRegion(), dto.getLanguage()));
        }
      }
      log.info("Ended movie data indexing...");
    } catch (IllegalStateException | FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (DataIntegrityViolationException e) {
      if (dto != null) {
        log.error("Error at : " + dto.getTitleId(), DataIntegrityViolationException.class);
      }
      e.printStackTrace();
    } finally {
      this.indexStateService.setIndexState(this.state);
    }
  }

  private void indexMoviesDataBasics(int skipLines) {
    File file = this.imdbFileRepository.getMovieDataBasicsFile();
    try {
      CSVReader reader = buildReader(file);
      String[] line;
      log.info("Started complementary movie data indexing...");
      while ((line = reader.readNext()) != null) {
        final MovieBasicsDto dto = this.movieDataMapper.mapToMovieBasicsDto(line);
        Optional<List<MovieData>> movieListData = this.movieDataRepository.findByTitleId(dto.getTconst());
        if (movieListData.isPresent()) {
          final List<MovieData> movieData = movieListData.get();
          for (MovieData data : movieData) {
            data.setAdult(dto.isAdult());
            data.setStartYear(dto.getStartYear());
            data.setEndYear(dto.getEndYear());
            data.setRuntime(dto.getRuntimeMinutes());
            data.setType(new Type(dto.getTitleType()));
            this.movieDataRepository.save(data);
          }
        }
      }
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

  // private void indexRatings() {
  // throw new NotImplementedException();
  // }

  // private void indexSeries() {
  // throw new NotImplementedException();
  // }

  // private void indexPersons() {
  // throw new NotImplementedException();
  // }

  // private void indexCrew() {
  // throw new NotImplementedException();
  // }

  // private void indexPrincipals() {
  // throw new NotImplementedException();
  // }

  private CSVReader buildReader(File file) throws FileNotFoundException {
    CSVParser parser = new CSVParserBuilder().withEscapeChar('\0').withQuoteChar('\0').withSeparator('\t').build();
    return new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).withSkipLines(1).build();
  }

}
