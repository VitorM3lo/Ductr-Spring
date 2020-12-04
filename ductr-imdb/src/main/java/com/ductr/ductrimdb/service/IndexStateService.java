package com.ductr.ductrimdb.service;

import java.util.Optional;

import com.ductr.ductrimdb.entity.IndexState;
import com.ductr.ductrimdb.repository.IndexStateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexStateService {
  
  @Autowired
  private IndexStateRepository repository;

  public IndexState getIndexState() {
    Optional<IndexState> optional = repository.findById(1);
    return optional.isPresent() ? optional.get() : null;
  }

  public void setIndexState(IndexState state) {
    repository.save(state);
  }

}
