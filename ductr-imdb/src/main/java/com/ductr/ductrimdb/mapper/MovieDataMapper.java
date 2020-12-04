package com.ductr.ductrimdb.mapper;

import java.util.Arrays;

import com.ductr.ductrimdb.dto.MovieBasicsDto;
import com.ductr.ductrimdb.dto.MovieDataDto;

import org.springframework.stereotype.Service;

@Service
public class MovieDataMapper {
  
  public MovieDataDto mapToMovieDataDto(String[] line) {
    MovieDataDto dto = new MovieDataDto();
    verifyNullChars(line);
    dto.setTitleId(line[0]);
    dto.setOrdering(Integer.parseInt(line[1]));
    dto.setTitle(line[2]);
    dto.setRegion(line[3]);
    dto.setLanguage(line[4]);
    dto.setTypes(Arrays.asList(line[5].split(" ")));
    dto.setAttributes(Arrays.asList(line[6].split(" ")));
    dto.setOriginalTitle(Boolean.parseBoolean(line[7]));
    return dto;
  }

  public MovieBasicsDto mapToMovieBasicsDto(String[] line) {
    MovieBasicsDto dto = new MovieBasicsDto();
    verifyNullChars(line);
    dto.setTconst(line[0]);
    dto.setTitleType(line[1]);
    dto.setPrimaryTitle(line[2]);
    dto.setOriginalTitle(line[3]);
    dto.setAdult(Boolean.parseBoolean(line[4]));
    dto.setStartYear(Integer.parseInt(line[5]));
    dto.setEndYear(Integer.parseInt(line[6]));
    dto.setRuntimeMinutes(Integer.parseInt(line[7]));
    dto.setGenres(Arrays.asList(line[0].split(" ")));
    return dto;
  }

  private void verifyNullChars(String[] line) {
    for (String string : line) {
      if (string == "\\N" || string == "N") {
        string = null;
      }
    }
  }

}
