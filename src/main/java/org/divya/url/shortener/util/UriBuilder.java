package org.divya.url.shortener.util;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
public class UriBuilder {

  public static URI buildUriPath(String host, String port, String path,
      Map<String, String> queryParams) {
    Map<String, List<String>> collect = queryParams.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> List.of(e.getValue())));
    URI url;
    if (StringUtils.isEmpty(port)) {
      url = new DefaultUriBuilderFactory().builder()
          .scheme("http")
          .host(host)
          .path(path)
          .queryParams(new LinkedMultiValueMap<>(collect))
          .build()
          .normalize();
    } else {
      url = new DefaultUriBuilderFactory().builder()
          .scheme("http")
          .host(host)
          .port(port)
          .path(path)
          .queryParams(new LinkedMultiValueMap<>(collect)).build().normalize();
    }
    log.info("url formed is {}", url.toString());
    return url;
  }

}
