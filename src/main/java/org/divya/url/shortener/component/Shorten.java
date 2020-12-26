package org.divya.url.shortener.component;

import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.springframework.stereotype.Component;

@Component
public interface Shorten {

  String shorten(UrlShortenRequest url, String httpHeaders);

}
