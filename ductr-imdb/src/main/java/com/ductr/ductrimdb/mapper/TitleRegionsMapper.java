package com.ductr.ductrimdb.mapper;

import com.ductr.ductrentity.entities.TitleRegion;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TitleRegionsMapper implements FieldSetMapper<TitleRegion> {

  @Override
  public TitleRegion mapFieldSet(FieldSet fieldSet) throws BindException {
    if (fieldSet == null) {
      return null;
    }

    TitleRegion titleRegion = new TitleRegion();
    titleRegion.setTconst(fieldSet.readString("titleId"));
    titleRegion.setTitle(fieldSet.readString("title"));
    titleRegion.setRegion(!fieldSet.readString("region").equals("\\N") ? fieldSet.readString("region") : null);
    titleRegion.setOriginal(
        !fieldSet.readString("isOriginalTitle").equals("\\N") ? fieldSet.readInt("isOriginalTitle") == 1 : false);
    return titleRegion;
  }

}
