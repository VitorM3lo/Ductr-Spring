package com.ductr.ductrmain.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.ductr.ductrmain.dto.SubtitleDataDto;
import com.ductr.ductrmain.entity.FormatEnum;
import com.ductr.ductrmain.entity.Subtitle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Repository
public class OpenSubtitlesRepository {

  @Value("${opensubtitles.old.endpoint}")
  String opensubtitlesEndpoint;

  public List<SubtitleDataDto> getSubtitles(String videoHash, String languageCode) {
    final String uri = opensubtitlesEndpoint + "/moviehash-" + videoHash
        + (languageCode != null ? "/sublanguageid-" + languageCode : "");
    try {
      List<SubtitleDataDto> result = WebClient.create(uri).get().header("Accept", MediaType.APPLICATION_JSON_VALUE)
          .header("User-Agent", "VLSub").retrieve().bodyToFlux(SubtitleDataDto.class).collectList().block();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public List<SubtitleDataDto> getSubtitles(File video, String languageCode) {
    final String uri = opensubtitlesEndpoint + "/query-" + video.getName().toLowerCase()
        + (languageCode != null ? "/sublanguageid-" + languageCode : "");
    try {
      List<SubtitleDataDto> result = WebClient.create(uri).get().header("Accept", MediaType.APPLICATION_JSON_VALUE)
          .header("User-Agent", "VLSub").retrieve().bodyToFlux(SubtitleDataDto.class).collectList().block();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public Subtitle downloadSubtitle(String url, String videoPath) {
    Path pathCompressedFile = Paths.get(videoPath.replace(FormatEnum.MP4.getType(), FormatEnum.GZIP.getType()));
    Path pathSubtitleFile = Paths.get(videoPath.replace(FormatEnum.MP4.getType(), FormatEnum.SRT.getType()));
    Flux<DataBuffer> compressedFile = WebClient.create(url).get().retrieve().bodyToFlux(DataBuffer.class);
    DataBufferUtils.write(compressedFile, pathCompressedFile, StandardOpenOption.CREATE).block();
    try {
      GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(pathCompressedFile.toFile()));
      FileOutputStream fileOutputStream = new FileOutputStream(pathSubtitleFile.toFile());
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }
      gzipInputStream.close();
      fileOutputStream.close();
      pathCompressedFile.toFile().delete();
      return new Subtitle(pathSubtitleFile.toFile().getName(), pathSubtitleFile.toFile().getAbsolutePath());
    } catch (IOException e) {
      return null;
    }
  }

}
