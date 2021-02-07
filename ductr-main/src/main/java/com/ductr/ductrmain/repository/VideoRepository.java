package com.ductr.ductrmain.repository;

import com.ductr.ductrmain.entity.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

  Video findByName(String name);

  Video findByHash(String hash);

}
