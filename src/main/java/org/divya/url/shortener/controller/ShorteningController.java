package org.divya.url.shortener.controller;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.divya.url.shortener.component.Shorten;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
@Slf4j
public class ShorteningController {

  private final Shorten shorten;

  public ShorteningController(Shorten shorten) {
    this.shorten = shorten;
  }

  @PostMapping(
      value = "/generate",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.TEXT_HTML_VALUE})
  public String shortenUrl(@RequestBody UrlShortenRequest urlShortenRequest,
      @RequestHeader HttpHeaders headers) throws JsonProcessingException {
    log.info("request received {}", urlShortenRequest);

    headers.forEach((key, value) -> {
      log.info("Header received from the request inside controller {} ", kv(key, value));
    });

    return shorten.shorten(urlShortenRequest, headers);
  }

  @GetMapping(value = "access/{shortenedUrl}")
  public String getVerification(
      @PathVariable(value = "shortenedUrl") String shortenedUrl,
      @RequestHeader HttpHeaders headers) {
    log.info("Redirect to the original with short url{} ", shortenedUrl);

    return shorten.redirect(shortenedUrl, headers);
  }
}