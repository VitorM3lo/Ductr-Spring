package com.ductr.ductr.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.ductr.ductr.dto.VideoDto;
import com.ductr.ductr.entity.FormatEnum;
import com.ductr.ductr.entity.Video;
import com.ductr.ductr.repository.VideoRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import utils.FileUtils;

@Service
public class VideoService {

  @Value("${folderpath}")
  private String folderPath;

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private ModelMapper modelMapper;

  
  public List<VideoDto> getAll() {
    List<Video> videos = (List<Video>) this.videoRepository.findAll();
    if (videos == null || videos.size() == 0) {
      this.indexVideoFiles(folderPath);
      videos = (List<Video>) this.videoRepository.findAll();
    }
    return videos.stream().map(vf -> this.modelMapper.map(vf, VideoDto.class))
        .collect(Collectors.toList());
  }

  public Video getByName(String name) {
    return (Video) this.videoRepository.findByName(name);
  }

  public Video getById(int id) {
    try {
      return this.videoRepository.findById(Integer.valueOf(id)).get();
    } catch (NoSuchElementException e) {
      this.indexVideoFiles(folderPath);
      return this.videoRepository.findById(Integer.valueOf(id)).get();
    }
  }

  @SuppressWarnings("unchecked")
  private void indexVideoFiles(String folder) {
    this.videoRepository.saveAll((List<Video>) this.fileUtils.getFiles(folder, FormatEnum.MP4));
  }

}
