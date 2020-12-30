package org.divya.url.shortener.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import org.divya.url.shortener.clients.rest.GenericUrlVerificationClient;
import org.divya.url.shortener.errors.ShorteningExceptions;
import org.divya.url.shortener.model.db.UrlShortenerModel;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.divya.url.shortener.model.repos.UrlShortenerRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
class UrlShortenServiceTest {

  @Mock
  private GenericUrlVerificationClient genericUrlVerificationClientMock;

  @Mock
  private UrlShortenerRepository urlShortenerRepositoryMock;

  private UrlShortenService urlShortenServiceMock;

  private static Stream<Arguments> provideUrlAndSize() {
    return Stream.of(
        Arguments.of("https://www.baeldung.com/parameterized-tests-junit-5", 2),
        Arguments.of("https://www.baeldung.com/spring-boot-docker-images", 3),
        Arguments.of(" https://kubernetes.io/docs/tutorials/stateless-application/expose-external-ip-address/", 4),
        Arguments
            .of("https://stackoverflow.com/questions/41485751/java-8-optional-ifpresent-return-object-orelsethrow-exception",
                15)
    );
  }

  @BeforeEach
  void init() {
    String allowedClients = "divya-client1";
    urlShortenServiceMock = new UrlShortenService(genericUrlVerificationClientMock, allowedClients,
        urlShortenerRepositoryMock);
  }

  @ParameterizedTest
  @MethodSource("provideUrlAndSize")
  @DisplayName("Should shorten url")
  void should_ShortenUrl_WhenUrlRequestIsProvided(String urlToBeShortened, int maxLength) {

    UrlShortenRequest urlShortenRequest = UrlShortenRequest.builder()
        .url(urlToBeShortened)
        .daysToPersist(2)
        .isRest(false)
        .maxLength(maxLength)
        .build();
    when(urlShortenerRepositoryMock.save(any(UrlShortenerModel.class))).thenReturn(UrlShortenerModel.builder().build());
    String shorten = urlShortenServiceMock.shorten(urlShortenRequest, getHeaders("divya-client1"));
    Assert.assertEquals(shorten.length(), maxLength);
    verify(urlShortenerRepositoryMock, times(1)).save(any(UrlShortenerModel.class));
  }

  @Test
  @DisplayName("Should throw exception")
  void should_ThrowInvalidClientException_GivenInvalidClient() {

    UrlShortenRequest urlShortenRequest = UrlShortenRequest.builder()
        .url("urlToBeShortened")
        .daysToPersist(2)
        .isRest(false)
        .maxLength(3)
        .build();

    ShorteningExceptions shorteningExceptions = assertThrows(ShorteningExceptions.class, () -> {
      String shorten = urlShortenServiceMock.shorten(urlShortenRequest, getHeaders("divya-client"));
    });

    String expectedMessage = "This X-client is not allowed client";
    String actualMessage = shorteningExceptions.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  @DisplayName("Should get url from short url")
  void should_redirect_GivenShortUrl() {

    String originalUrl = "http://localhost:8080/OriginalUrl";
    String iamShortUrl = "IamShortUrl";
    when(urlShortenerRepositoryMock.findByShortenedUrl(anyString())).thenReturn(Optional.of(UrlShortenerModel.builder()
        .originalUrl(originalUrl)
        .shortenedUrl(iamShortUrl)
        .build()));

    String outComeUrl = urlShortenServiceMock
        .redirect(iamShortUrl, getHeaders("anyHeaderNotValidatedHere"));

    assertEquals(originalUrl, outComeUrl);

  }

  private HttpHeaders getHeaders(String headerValue) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("X-Client", headerValue);
    return httpHeaders;
  }

}