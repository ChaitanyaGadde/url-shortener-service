package org.divya.url.shortener.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = UrlShortenRequest.Builder.class)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlShortenRequest {

  private final String url;
  private final int maxLength;
  private final int daysToPersist;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

  }

}
