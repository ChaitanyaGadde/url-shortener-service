package org.divya.url.shortener.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.net.URI;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = UrlShortenResponse.Builder.class)
@Getter
public class UrlShortenResponse {

  public URI shortUrl;
  public String shortKey;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

  }

}
