package org.divya.url.shortener.service;

import lombok.extern.slf4j.Slf4j;
import org.divya.url.shortener.component.Shorten;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlShortenService implements Shorten {

  @Override
  public String shorten(UrlShortenRequest url, String httpHeaders) {
    return null;
  }

}
