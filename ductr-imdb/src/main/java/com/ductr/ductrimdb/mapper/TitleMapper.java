package com.ductr.ductrimdb.mapper;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ductr.ductrimdb.entity.Genre;
import com.ductr.ductrimdb.entity.Title;
import com.ductr.ductrimdb.entity.Type;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TitleMapper implements FieldSetMapper<Title> {

  @Override
  public Title mapFieldSet(FieldSet fieldSet) throws BindException {
    if (fieldSet == null) {
      return null;
    }

    Title title = new Title();
    String helper = null;

    title.setTconst(fieldSet.readString("tconst"));
    title.setTitleType(new Type(fieldSet.readString("titleType")));
    title.setPrimaryTitle(fieldSet.readString("primaryTitle"));
    title.setOriginalTitle(fieldSet.readString("originalTitle"));
    helper = fieldSet.readString("isAdult");
    title.setAdult(!helper.equals("\\N") ? Integer.parseInt(helper) == 1 : false);
    helper = fieldSet.readString("startYear");
    title.setStartYear(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
    helper = fieldSet.readString("endYear");
    title.setEndYear(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
    helper = fieldSet.readString("runtimeMinutes");
    title.setRuntimeMinutes(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
    title.setGenres(Arrays.asList(fieldSet.readString("genres").split(",")).stream().map(g -> new Genre(g))
        .collect(Collectors.toList()));

    return title;
  }
}