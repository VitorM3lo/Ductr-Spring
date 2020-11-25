package com.ductr.ductr.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import com.ductr.ductr.dto.VideoDto;
import com.ductr.ductr.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import utils.FileUtils;

@RestController
public class StreamController {

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private VideoService videoService;

  @CrossOrigin
  @GetMapping("/videos")
  List<VideoDto> getFiles() {
    return this.videoService.getAll();
  }

  @CrossOrigin
  @GetMapping("/stream")
  ResponseEntity<ResourceRegion> getStream(@RequestParam int id, @RequestHeader HttpHeaders headers) {
    try {
      File videoFile = new File(this.videoService.getById(id).getPath());
      UrlResource video = new FileUrlResource(videoFile.getAbsolutePath());
      ResourceRegion videoSnippet = this.fileUtils.createSnippet(video, headers);
      return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
          .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
          .body(videoSnippet);
    } catch (NullPointerException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}