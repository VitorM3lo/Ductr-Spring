package com.ductr.ductrmain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SubtitleDataDto {

  @JsonAlias("IDSubtitleFile")
  private String id;

  @JsonAlias("IDMovieImdb")
  private int imdbId;

  @JsonAlias("MovieReleaseName")
  private String release;

  @JsonAlias("SubDownloadLink")
  private String url;

  @JsonAlias("SubActualCD")
  private int quantity;

  @JsonAlias("SubFileName")
  private String filename;

  @JsonAlias("ISO639")
  private String languageCode;

  @JsonAlias("SubLanguageID")
  private String languageID;

  @JsonGetter("id")
  public String getId() {
    return id;
  }

  @JsonGetter("imdbId")
  public int getImdbId() {
    return imdbId;
  }

  @JsonGetter("filename")
  public String getFilename() {
    return filename;
  }

  @JsonGetter("languageCode")
  public String getLanguageCode() {
    return languageCode;
  }

  @JsonGetter("languageId")
  public String getLanguageID() {
    return languageID;
  }

  @JsonGetter("releaseName")
  public String getRelease() {
    return release;
  }

  @JsonGetter("downloadUrl")
  public String getUrl() {
    return url;
  }
}
