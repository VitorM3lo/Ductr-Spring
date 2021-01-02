package com.ductr.ductrimdb.processor;

import java.util.Arrays;

import com.ductr.ductrimdb.entity.TitleRegionData;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class TitleRegionDataProcessor implements ItemProcessor<TitleRegionData, TitleRegionData> {

  @Value("${regions}")
  private String[] regions;

  @Override
  public TitleRegionData process(TitleRegionData item) throws Exception {
    return Arrays.asList(regions).contains(item.getRegion()) ? item : null;
  }

}
