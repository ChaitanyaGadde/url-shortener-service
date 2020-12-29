package org.divya.url.shortener.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.divya.url.shortener.component.ShortenInterface;
import org.divya.url.shortener.model.dto.UrlShortenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ContextConfiguration(classes = ShorteningController.class)
class ShorteningControllerTest {

  private final String host = "localhost";
  private final String port = "path";
  private final String apiPath = "getpath";
  @MockBean
  ShortenInterface shorten;
  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Initiate shorten request")
  void should_initShortenRequest() throws Exception {

    when(shorten.shorten(any(UrlShortenRequest.class), any(HttpHeaders.class))).thenReturn("abcedf");

    mockMvc.perform(post("/shorten/generate").header("headers", new HttpHeaders())
        .header("X-Transaction-ID", "dtiofjer-sdfd-dff")
        .content(getRequestBuild())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

  }

  @Test
  @DisplayName("Get redirected with short url")
  void should_RedirectShortUrl() throws Exception {

    when(shorten.redirect(anyString(), any(HttpHeaders.class))).thenReturn("abcedf");

    mockMvc.perform(get("/shorten/access/1234").header("headers", new HttpHeaders())
        .header("X-Transaction-ID", "dtiofjer-sdfd-dff")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection());

  }

  private String getRequestBuild() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(UrlShortenRequest.builder()
        .daysToPersist(2)
        .url("http://localhost:8080/url/url")
        .build());
  }

}