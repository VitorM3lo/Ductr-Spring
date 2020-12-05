package com.ductr.ductrimdb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import com.ductr.ductrimdb.dto.MovieBasicsDto;
import com.ductr.ductrimdb.dto.MovieDataDto;
import com.ductr.ductrimdb.entity.Genre;
import com.ductr.ductrimdb.entity.IndexState;
import com.ductr.ductrimdb.entity.MovieData;
import com.ductr.ductrimdb.entity.StepEnum;
import com.ductr.ductrimdb.entity.Type;
import com.ductr.ductrimdb.mapper.MovieDataMapper;
import com.ductr.ductrimdb.repository.IMDbFileRepository;
import com.ductr.ductrimdb.repository.MovieDataRepository;
import com.ductr.ductrimdb.repository.MovieGenreRepository;
import com.ductr.ductrimdb.repository.MovieTypeRepository;
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
  private MovieTypeRepository movieTypeRepository;

  @Autowired
  private MovieGenreRepository movieGenreRepository;

  @Autowired
  private IndexStateService indexStateService;

  @Autowired
  private MovieDataMapper movieDataMapper;

  @Value("${regions}")
  List<String> regions;

  @Value("${save.checkpoint}")
  int checkpoint;

  private IndexState state = null;

  @PreDestroy
  private void shutdown() {
    this.indexStateService.setIndexState(this.state);
  }

  @Scheduled(cron = "0 1 * * * SUN")
  public void index() {
    this.state = this.indexStateService.getIndexState();
    if (this.state != null) {
      indexStepped(this.state);
    } else {
      indexSequentially();
    }
  }

  private void indexStepped(IndexState state) {
    switch (state.getStep()) {
      case 1:
        this.indexMovieData(state != null ? state.getLine() : -1);
      case 2:
        if (state.getStep() + 1 == StepEnum.MOVIE_BASICS.getStep()) {
          state.setStep(StepEnum.MOVIE_BASICS.getStep());
          state.setLine(-1);
        }
        this.indexMoviesDataBasics(state != null ? state.getLine() : -1);
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

      configureReader(csvReader, skipLines, StepEnum.MOVIE_DATA.getStep());

      String[] line;
      log.info("Started movie data indexing...");
      while ((line = csvReader.readNext()) != null) {
        if (this.state.getLine() % checkpoint == 0) {
          shutdown();
        }
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
    MovieBasicsDto dto = null;

    File file = this.imdbFileRepository.getMovieDataBasicsFile();

    try {
      CSVReader csvReader = buildReader(file);
      configureReader(csvReader, skipLines, StepEnum.MOVIE_BASICS.getStep());

      String[] line;
      log.info("Started complementary movie data indexing...");
      while ((line = csvReader.readNext()) != null) {
        dto = this.movieDataMapper.mapToMovieBasicsDto(line);
        this.state.setLine(this.state.getLine() + 1);
        Optional<List<MovieData>> movieListData = this.movieDataRepository.findByTitleId(dto.getTconst());
        if (movieListData.isPresent()) {
          final List<MovieData> movieData = movieListData.get();
          for (MovieData data : movieData) {
            data.setAdult(dto.isAdult());
            data.setStartYear(dto.getStartYear());
            data.setEndYear(dto.getEndYear());
            data.setRuntime(dto.getRuntimeMinutes());
            Optional<Type> fetchedType = this.movieTypeRepository.findById(dto.getTitleType());
            if (!fetchedType.isPresent()) {
              Type type = new Type(dto.getTitleType());
              this.movieTypeRepository.save(type);
            } else {
              data.setType(fetchedType.get());
            }
            data.setGenres(dto.getGenres().stream().map(g -> {
              Optional<Genre> fetchedGenre = this.movieGenreRepository.findById(g);
              if (!fetchedGenre.isPresent()) {
                return this.movieGenreRepository.save(new Genre(g));
              } else {
                return fetchedGenre.get();
              }
            }).collect(Collectors.toList()));
            this.movieDataRepository.save(data);
          }
        }
      }
      log.info("Ended complementary movie data indexing...");
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    } catch (DataIntegrityViolationException e) {
      e.printStackTrace();
    } finally {
      if (dto != null) {
        log.error("Error at : " + dto.getTconst(), DataIntegrityViolationException.class);
      }
      this.indexStateService.setIndexState(this.state);
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

  private void configureReader(CSVReader reader, int skipLines, int step) throws IOException {
    if (skipLines != -1) {
      // exclude header
      reader.skip(skipLines - 1);
    } else {
      this.state = new IndexState(1, step, reader.getSkipLines());
    }
  }

  private CSVReader buildReader(File file) throws FileNotFoundException {
    CSVParser parser = new CSVParserBuilder().withEscapeChar('\0').withQuoteChar('\0').withSeparator('\t').build();
    return new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).withSkipLines(1).build();
  }

}
