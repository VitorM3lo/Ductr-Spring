package com.ductr.ductrimdb.mapper;

import java.util.Arrays;

import com.ductr.ductrimdb.dto.MovieDataDto;

import org.springframework.stereotype.Service;

@Service
public class MovieDataMapper {
  
  public MovieDataDto mapToMovieDataDto(String[] line) {
    MovieDataDto dto = new MovieDataDto();
    for (String string : line) {
      if (string == "\\N") {
        string = null;
      }
    }
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

}
