package com.ductr.ductr.repository;

import com.ductr.ductr.entity.Video;

import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Integer> {

  Video findByName(String name);

  Video findByHash(String hash);

}
