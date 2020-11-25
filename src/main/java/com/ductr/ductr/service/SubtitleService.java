package com.ductr.ductr.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.ductr.ductr.dto.SubtitleDataDto;
import com.ductr.ductr.entity.Subtitle;
import com.ductr.ductr.entity.SubtitleData;
import com.ductr.ductr.entity.Video;
import com.ductr.ductr.repository.OpenSubtitlesRepository;
import com.ductr.ductr.repository.SubtitleDataRepository;
import com.ductr.ductr.repository.SubtitleRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import utils.FileUtils;

@Service
public class SubtitleService {

  @Autowired
  OpenSubtitlesRepository openSubtitlesRepository;

  @Autowired
  SubtitleRepository subtitleRepository;

  @Autowired
  SubtitleDataRepository subtitleDataRepository;

  @Autowired
  VideoService fileService;

  @Autowired
  FileUtils fileUtils;

  @Autowired
  ModelMapper modelMapper;

  @Value("${folderpath}")
  String folderPath;

  public List<SubtitleDataDto> getSubtitles(int id, String languageCode) {
    Video video = this.fileService.getById(id);
    String hash = this.fileUtils.createHash(video.getPath());
    List<SubtitleData> subtitles = this.subtitleDataRepository.getByMovieHashAndLanguageCode(id, languageCode);
    if (subtitles.isEmpty()) {
      List<SubtitleDataDto> result = this.openSubtitlesRepository.getSubtitles(hash, languageCode);
      if (result == null || result.isEmpty()) {
        result = this.openSubtitlesRepository.getSubtitles(new File(video.getPath()), languageCode);
      }
      try {
        final List<SubtitleData> subtitleDatas = result.stream()
            .map(osd -> this.modelMapper.map(osd, SubtitleData.class)).collect(Collectors.toList());
        subtitleDatas.forEach(s -> s.addMovieId(id));
        this.subtitleDataRepository.saveAll(subtitleDatas);
        return result;
      } catch (NullPointerException e) {
        return null;
      }
    }
    return subtitles.stream().map(s -> this.modelMapper.map(s, SubtitleDataDto.class)).collect(Collectors.toList());
  }

  public Resource getSubtitleResource(String subtitleId, int movieId) {
    Subtitle subtitle = null;
    try {
      SubtitleData subtitleData = this.subtitleDataRepository.findById(subtitleId);
      subtitle = getSubtitle(subtitleData);
      if (subtitle == null) {
        Video video = this.fileService.getById(movieId);
        subtitle = this.openSubtitlesRepository.downloadSubtitle(subtitleData.getUrl(), video.getPath());
        subtitle.setSubtitleData(subtitleData);
        this.subtitleRepository.save(subtitle);
      }
      return new ByteArrayResource(Files.readAllBytes(Paths.get(subtitle.getPath())));
    } catch (NoSuchElementException e) {
      return null;
    } catch (IOException e) {
      this.subtitleRepository.delete(subtitle);
      return null;
    }
  }

  private Subtitle getSubtitle(SubtitleData data) {
    try {
      return this.subtitleRepository.findBySubtitleData(data);
    } catch (NoSuchElementException e) {
      return null;
    }
  }

}
