package org.divya.url.shortener.component;

import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public interface Shorten {

  String shorten(UrlShortenRequest url, HttpHeaders httpHeaders);

  String redirect(String shortUrl, HttpHeaders httpHeaders);

}
