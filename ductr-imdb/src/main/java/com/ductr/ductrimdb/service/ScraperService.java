package com.ductr.ductrimdb.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScraperService {

  @Value("${imdbEndpoint}")
  private String imdbEndpoint;

  public String getPosterLink(String tconst) {
    try {
      Document doc = Jsoup.connect(imdbEndpoint + tconst).get();

      Elements posterLink = doc.getElementsByTag("meta");
      for (Element element : posterLink) {
        if (element.hasAttr("property") && element.attr("property").equalsIgnoreCase("og:image")) {
          String[] link = element.attr("content").split("@");
          return link[0] + "@._V1_FMjpg_UX1000_.jpg";
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      log.error(e.getMessage(), IOException.class);
    }
    return null;
  }

  public String getDescription(String tconst) {
    try {
      Document doc = Jsoup.connect(imdbEndpoint + tconst).get();

      Elements posterLink = doc.getElementsByTag("meta");
      for (Element element : posterLink) {
        if (element.hasAttr("property") && element.attr("property").equalsIgnoreCase("og:description")) {
          return element.attr("content");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      log.error(e.getMessage(), IOException.class);
    }
    return null;
  }

}
