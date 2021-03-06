package org.divya.url.shortener.service;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.divya.url.shortener.clients.rest.GenericUrlVerificationClient;
import org.divya.url.shortener.component.ShortenInterface;
import org.divya.url.shortener.errors.ShorteningExceptions;
import org.divya.url.shortener.model.db.UrlShortenerModel;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.divya.url.shortener.model.repos.UrlShortenerRepository;
import org.divya.url.shortener.util.UrlHashGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlShortenService implements ShortenInterface {

  private final GenericUrlVerificationClient genericUrlVerificationClient;
  private final String allowedClients;
  private final UrlShortenerRepository urlShortenerRepository;

  public UrlShortenService(
      GenericUrlVerificationClient genericUrlVerificationClient,
      @Value("${whitelisted.X-Clients}") String allowedClients,
      UrlShortenerRepository urlShortenerRepository) {
    this.genericUrlVerificationClient = genericUrlVerificationClient;
    this.allowedClients = allowedClients;
    this.urlShortenerRepository = urlShortenerRepository;
  }

  @Override
  public String shorten(UrlShortenRequest url, HttpHeaders httpHeaders) {

    clientValidations(httpHeaders);
    log.debug("Client Validations passed");

    if (url.isRest()) {
      genericUrlVerificationClient.isValidUrl(URI.create(url.getUrl()), httpHeaders);
      log.debug("url returned 200 ok about to perform shortening");
    }

    return performShortening(url, httpHeaders.getFirst("X-Client"));
  }

  @Override
  public String redirect(String shortUrl, HttpHeaders httpHeaders) {
    log.info("into get redirect method url shortener");
    Optional<UrlShortenerModel> byShortenedUrl = urlShortenerRepository.findByShortenedUrl(shortUrl);
    return byShortenedUrl
        .map(UrlShortenerModel::getOriginalUrl)
        .orElseThrow(RuntimeException::new);
  }

  private String performShortening(UrlShortenRequest url, String allowedClient) {

    try {
      String uniqueParams = url.getUrl()
          + allowedClient
          + url.getDaysToPersist();

      UrlHashGenerator urlHashGenerator = new UrlHashGenerator();
      String generatedHash = urlHashGenerator.generateToken(uniqueParams)
          .substring(0, url.getMaxLength());
      log.info("Generated secure and Hashed url is {}", generatedHash);
      urlShortenerRepository.save(buildModel(url, generatedHash, allowedClient));
      return generatedHash;
    } catch (NoSuchAlgorithmException e) {
      log.error("Exception hit {}", e.getMessage());
    }
    return "";
  }

  private UrlShortenerModel buildModel(UrlShortenRequest url, String generatedHash, String allowedClient) {
    return UrlShortenerModel.builder()
        .clientName(allowedClient)
        .daysToPersist(url.getDaysToPersist())
        .originalUrl(url.getUrl())
        .shortenedUrl(generatedHash)
        .build();
  }

  private void clientValidations(HttpHeaders httpHeaders) {
    if (!httpHeaders.containsKey("X-Client")) {
      throw new ShorteningExceptions(HttpStatus.BAD_REQUEST, "No X-Client present at headers, it is required");
    }
    String clientCaller = httpHeaders.getFirst("X-Client");

    if (StringUtils.isNotEmpty(clientCaller) &&
        Arrays.stream(allowedClients.split(","))
            .filter(Objects::nonNull)
            .noneMatch(s -> s.equalsIgnoreCase(clientCaller))) {
      throw new ShorteningExceptions(HttpStatus.BAD_REQUEST, "This X-client is not allowed client");
    }

  }

}
