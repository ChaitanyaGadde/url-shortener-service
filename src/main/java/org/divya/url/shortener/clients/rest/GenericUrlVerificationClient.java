package org.divya.url.shortener.clients.rest;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.divya.url.shortener.errors.ShorteningExceptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class GenericUrlVerificationClient {

  private final WebClient webClient;

  public GenericUrlVerificationClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public boolean isValidUrl(URI url, HttpHeaders httpHeaders) {
    return validateResponse(callUrl(url, httpHeaders));
  }

  private boolean validateResponse(ClientResponse clientResponse) {
    if (!clientResponse.statusCode().is2xxSuccessful()) {
      log.error("Client Response code received is {}, body {} and error message is {}",
          kv(clientResponse.statusCode().toString(), clientResponse.statusCode().value()),
          clientResponse.bodyToMono(String.class).block(), "Url Calling returns non health response");
      throw new ShorteningExceptions(clientResponse.statusCode(), "Url Calling returns non health response");
    } else {
      log.info("Client Response code received is {}",
          kv(clientResponse.statusCode().toString(), clientResponse.statusCode().value()));
    }
    return true;
  }

  private ClientResponse callUrl(URI url, HttpHeaders httpHeaders) {

    return webClient
        .method(HttpMethod.GET)
        .uri(url)
        .headers(convertToConsumerHeaders(httpHeaders))
        .body(BodyInserters.fromValue(""))
        .exchange()
        .doOnError(t -> {
          log.error("Caught exception while exchange webClient {}", t.getMessage());
          log.error(t.getMessage(), t);
        })
        .block();
  }

  private Consumer<HttpHeaders> convertToConsumerHeaders(HttpHeaders headers) {
    Consumer<HttpHeaders> httpHeadersConsumer = httpHeaders -> {
      httpHeaders.addAll(headers);
      httpHeaders.addIfAbsent("X-TRANSACTION-ID", UUID.randomUUID().toString());
      httpHeaders.addIfAbsent("X-Client", "shorten-service");
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    };
    return httpHeadersConsumer;
  }

}
