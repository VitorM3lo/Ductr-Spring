package com.ductr.ductrmain.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import utils.FileUtils;

@Configuration
public class AppConfig {

  @Bean
  public FileUtils getFileUtils() {
    return new FileUtils();
  }

  @Bean
  public ModelMapper getModelMapper() {
    return new ModelMapper();
  }

}
