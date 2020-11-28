package com.ductr.ductrmain.repository;

import com.ductr.ductrmain.entity.Video;

import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Integer> {

  Video findByName(String name);

  Video findByHash(String hash);

}
