package org.divya.url.shortener.controller;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.divya.url.shortener.component.Shorten;
import org.divya.url.shortener.errors.ShorteningExceptions;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class ShorteningController {

  private final Shorten shorten;

  public ShorteningController(Shorten shorten) {
    this.shorten = shorten;
  }

  @PostMapping(
      value = "/play",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String shortenUrl(@RequestBody UrlShortenRequest urlShortenRequest,
      @RequestHeader HttpHeaders headers) throws JsonProcessingException {
    log.info("request received {}", urlShortenRequest);

    headers.forEach((key, value) -> {
      log.info("Header received from the request inside controller {} ", kv(key, value));
    });
    if (headers.containsKey("X-Client")) {
      String clientCaller = headers.getFirst("X-Client");
      return shorten.shorten(urlShortenRequest, clientCaller);
    }

    throw new ShorteningExceptions(HttpStatus.BAD_REQUEST, "Valid Client Header is Required to process request",
        "No X-Client Header Present");
  }
}