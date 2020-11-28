package com.ductr.ductrmain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class TorrentRepository {

  @Value("${torrent.endpoint}")
  String torrentEndpoint;

  public List<String> getTorrent() {
    return null;
  }

}
