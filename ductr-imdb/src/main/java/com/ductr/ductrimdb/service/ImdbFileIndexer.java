package com.ductr.ductrimdb.service;

import com.ductr.ductrimdb.repository.MovieDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImdbFileIndexer {
  
  @Autowired
  MovieDataRepository movieDataRepository;

}
