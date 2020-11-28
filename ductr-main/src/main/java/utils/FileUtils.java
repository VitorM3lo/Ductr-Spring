package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ductr.ductrmain.entity.BaseFile;
import com.ductr.ductrmain.entity.FormatEnum;
import com.ductr.ductrmain.entity.StatusEnum;
import com.ductr.ductrmain.entity.Subtitle;
import com.ductr.ductrmain.entity.Video;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;

public class FileUtils {

  public List<? extends BaseFile> getFiles(String folder, FormatEnum format) {
    try {
      List<String> paths = Files.walk(Paths.get(folder)).map(file -> file.toAbsolutePath().toString())
          .filter(file -> file.endsWith(format.getType())).collect(Collectors.toList());
      List<BaseFile> mediaFiles = new ArrayList<>();
      for (String absolutePath : paths) {
        switch (format) {
          case MP4:
            final File videoFile = new File(absolutePath);
            mediaFiles.add(new Video(videoFile.getName(), absolutePath, StatusEnum.DOWNLOADED, this.createHash(absolutePath)));
            break;
          case SRT:
            mediaFiles.add(new Subtitle(new File(absolutePath).getName(), absolutePath));
            break;
          default:
            break;
        }
      }
      return mediaFiles;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResourceRegion createSnippet(UrlResource videoFile, HttpHeaders headers) {
    try {
      long videoSize = videoFile.contentLength();
      List<HttpRange> range = headers.getRange();
      if (!range.isEmpty() && range.size() == 1) {
        long rangeStart = range.get(0).getRangeStart(videoSize);
        long rangeEnd = range.get(0).getRangeEnd(videoSize);
        // 1024 * 1024 = 1MB
        long rangeLength = Math.min(1024 * 1024, rangeEnd - rangeStart + 1);
        return new ResourceRegion(videoFile, rangeStart, rangeLength);
      } else {
        return new ResourceRegion(videoFile, 0, Math.min(1024 * 1024, videoSize));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String createHash(String videoPath) {
    File videoFile = new File(videoPath);
    try {
      return OpenSubtitlesHasher.computeHash(videoFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
