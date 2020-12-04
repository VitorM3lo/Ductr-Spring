package com.ductr.ductrimdb.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Repository
@Slf4j
public class IMDbFileRepository {

  @Autowired
  IndexStateRepository indexStateRepository;

  @Value("${imdb.dataset.endpoint}")
  private String url;

  @Value("${crew.dataset}")
  String crewDataset;

  @Value("${movie.basics.dataset}")
  String movieBasicsDataset;

  @Value("${principals.dataset}")
  String moviePrincipalsDataset;

  @Value("${person.dataset}")
  String personDataset;

  @Value("${episodes.dataset}")
  String episodesDataset;

  @Value("${ratings.dataset}")
  String ratingsDataset;

  @Value("${movie.dataset}")
  String movieDataDataset;

  @Value("${file.repository}")
  private String folder;

  public File getMovieDataBasicsFile() {
    return this.downloadFile(movieBasicsDataset);
  }

  public File getMovieDataFile() {
    return this.downloadFile(movieDataDataset);
  }

  public File getPersonFile() {
    return this.downloadFile(personDataset);
  }

  public File getRatingsFile() {
    return this.downloadFile(ratingsDataset);
  }

  public File getEpisodesFile() {
    return this.downloadFile(episodesDataset);
  }

  public File getPrincipalsFile() {
    return this.downloadFile(moviePrincipalsDataset);
  }

  public File getCrewFile() {
    return this.downloadFile(crewDataset);
  }

  private File downloadFile(String dataset) {

    Calendar today = Calendar.getInstance();
    Calendar fileCreation = Calendar.getInstance();

    Path pathCompressedFile = Paths.get(folder + dataset);
    Path pathDatasetFile = Paths.get(folder + dataset.replace(".gz", ""));

    BasicFileAttributes attribute = null;
    try {
      attribute = Files.readAttributes(pathDatasetFile, BasicFileAttributes.class);
      fileCreation.setTimeInMillis(attribute.lastModifiedTime().toMillis());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    final boolean createdToday = today.get(Calendar.DAY_OF_YEAR) == fileCreation.get(Calendar.DAY_OF_YEAR)
        && today.get(Calendar.YEAR) == fileCreation.get(Calendar.YEAR);

    if (pathDatasetFile.toFile().exists() && createdToday) {
      log.info("No need to update " + dataset);
      return pathDatasetFile.toFile();
    }

    this.indexStateRepository.deleteAll();

    log.info("Started download of: " + dataset);
    Flux<DataBuffer> compressedFile = WebClient.create(url + dataset).get().retrieve().bodyToFlux(DataBuffer.class);
    DataBufferUtils.write(compressedFile, pathCompressedFile, StandardOpenOption.CREATE).block();
    FileOutputStream fileOutputStream = null;
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(pathCompressedFile.toFile()))) {
      fileOutputStream = new FileOutputStream(pathDatasetFile.toFile());
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }
      return pathDatasetFile.toFile();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      log.info("Ended download of: " + dataset);
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        Files.delete(pathCompressedFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
