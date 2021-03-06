package com.ductr.ductrimdb.mapper;

import com.ductr.ductrentity.entities.Title;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RatingMapper implements FieldSetMapper<Title> {

  @Override
  public Title mapFieldSet(FieldSet fieldSet) throws BindException {
    if (fieldSet == null) {
      return null;
    }

    Title title = new Title();
    title.setTconst(fieldSet.readString("tconst"));
    title.setRating(fieldSet.readFloat("averageRating"));
    return title;
  }

}
