package com.ductr.ductrimdb.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

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

  @Value("${imdb.dataset.endpoint}")
  private String url;

  @Value("${file.repository}")
  private String folder;

  public File getFile(String filename) {
    return this.downloadFile(filename);
  }

  private File downloadFile(String dataset) {

    Calendar today = Calendar.getInstance();
    Calendar fileCreation = Calendar.getInstance();

    Path pathCompressedFile = Paths.get(folder + dataset);
    Path pathDatasetFileTemp = Paths.get(folder + dataset.replace(".gz", ".temp"));
    Path pathDatasetFile = Paths.get(folder + dataset.replace(".gz", ""));

    if (pathDatasetFile.toFile().exists()) {
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
    }

    log.info("Started download of: " + dataset);
    Flux<DataBuffer> compressedFile = WebClient.create(url + dataset).get().retrieve().bodyToFlux(DataBuffer.class);
    DataBufferUtils.write(compressedFile, pathCompressedFile, StandardOpenOption.CREATE).block();
    FileOutputStream fileOutputStream = null;
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(pathCompressedFile.toFile()))) {
      fileOutputStream = new FileOutputStream(pathDatasetFileTemp.toFile());
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }
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
    }

    try {
      if (pathDatasetFile.toFile().exists()) {
        BasicFileAttributes tempFileAttributes = Files.readAttributes(pathDatasetFileTemp, BasicFileAttributes.class);
        BasicFileAttributes datasetFileAttributes = Files.readAttributes(pathDatasetFile, BasicFileAttributes.class);
        if (tempFileAttributes.size() == datasetFileAttributes.size()) {
          Files.delete(pathDatasetFileTemp);
        } else {
          Files.delete(pathDatasetFile);
          Files.move(pathDatasetFileTemp.toFile().toPath(), pathDatasetFile.toFile().toPath(),
              StandardCopyOption.REPLACE_EXISTING);
        }
      } else {
        Files.move(pathDatasetFileTemp.toFile().toPath(), pathDatasetFile.toFile().toPath(),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    try {
      Files.delete(pathCompressedFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return pathDatasetFile.toFile();
  }

}
