package com.ductr.ductrimdb.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Repository
public class IMDbFileRepository {

  @Value("${imdb.dataset.endpoint}")
  private String url;

  @Value("${file.repository}")
  private String folder;

  public File getMovieDataBasicsFile(@Value("${movie.basics.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getMovieDataFile(@Value("${movie.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getPersonFile(@Value("${person.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getRatingsFile(@Value("${ratings.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getEpisodesFile(@Value("${episodes.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getPrincipalsFile(@Value("${principals.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  public File getCrewFile(@Value("${crew.dataset}") String dataset) {
    return this.DownloadFile(dataset);
  }

  private File DownloadFile(String dataset) {
    Path pathCompressedFile = Paths.get(folder + dataset);
    Path pathDatasetFile = Paths.get(folder + dataset.replace(".gz", ""));
    Flux<DataBuffer> compressedFile = WebClient.create(url + dataset).get().retrieve().bodyToFlux(DataBuffer.class);
    DataBufferUtils.write(compressedFile, pathCompressedFile, StandardOpenOption.CREATE).block();
    try {
      GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(pathCompressedFile.toFile()));
      FileOutputStream fileOutputStream = new FileOutputStream(pathDatasetFile.toFile());
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }
      gzipInputStream.close();
      fileOutputStream.close();
      pathCompressedFile.toFile().delete();
      return pathDatasetFile.toFile();
    } catch (IOException e) {
      return null;
    }
  }

}
