package org.divya.url.shortener.controller;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.divya.url.shortener.component.ShortenInterface;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.divya.url.shortener.model.dto.UrlShortenResponse;
import org.divya.url.shortener.util.UriBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  private final ShortenInterface shortenInterface;
  private final String host;
  private final String port;
  private final String apiPath;

  public ShorteningController(ShortenInterface shortenInterface, @Value("${shortUrl.app.host}") String host,
      @Value("${shortUrl.app.port}") String port,
      @Value("${shortUrl.app.apipath}") String apiPath) {
    this.shortenInterface = shortenInterface;
    this.host = host;
    this.port = port;
    this.apiPath = apiPath;
  }

  @PostMapping(
      value = "/generate",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public UrlShortenResponse shortenUrl(@RequestBody UrlShortenRequest urlShortenRequest,
      @RequestHeader HttpHeaders headers) throws JsonProcessingException {
    log.info("request received {}", urlShortenRequest);

    headers.forEach((key, value) -> {
      log.info("Header received from the request inside controller {} ", kv(key, value));
    });

    String shortenUrlKey = shortenInterface.shorten(urlShortenRequest, headers);
    return UrlShortenResponse.builder()
        .shortKey(shortenUrlKey)
        .shortUrl(UriBuilder.buildUriPath(host, port, "/shorten" + apiPath + shortenUrlKey, Map.of()))
        .build();
  }

  @GetMapping(value = "access/{shortenedUrl}")
  public ResponseEntity<Void> getVerification(
      @PathVariable(value = "shortenedUrl") String shortenedUrl,
      @RequestHeader HttpHeaders headers) {
    log.info("Redirect to the original with short url{} ", shortenedUrl);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(shortenInterface.redirect(shortenedUrl, headers)))
        .build();

  }
}