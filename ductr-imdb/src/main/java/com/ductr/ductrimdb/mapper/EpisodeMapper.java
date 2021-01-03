package com.ductr.ductrimdb.mapper;

import com.ductr.ductrimdb.entity.EpisodeData;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EpisodeMapper implements FieldSetMapper<EpisodeData> {

  @Override
  public EpisodeData mapFieldSet(FieldSet fieldSet) throws BindException {
    if (fieldSet == null) {
      return null;
    }

    EpisodeData data = new EpisodeData();
    data.setParentTconst(fieldSet.readString("parentTconst"));
    data.setTconst(fieldSet.readString("tconst"));
    data.setSeasonNumber(fieldSet.readString("seasonNumber").equals("\\N") ? -1 : fieldSet.readInt("seasonNumber"));
    data.setEpisodeNumber(fieldSet.readString("episodeNumber").equals("\\N") ? -1 : fieldSet.readInt("episodeNumber"));
    return data;

  }

}
