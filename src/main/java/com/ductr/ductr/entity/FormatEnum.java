package com.ductr.ductr.entity;

public enum FormatEnum {
  MP4("mp4"), SRT("srt"), GZIP("gz");

  private String type;

  FormatEnum(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
