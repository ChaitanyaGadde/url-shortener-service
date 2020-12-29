package org.divya.url.shortener.model.repos;

import java.util.List;
import java.util.Optional;
import org.divya.url.shortener.model.db.UrlShortenerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlShortenerRepository extends CrudRepository<UrlShortenerModel, Integer> {

  Optional<UrlShortenerModel> findByShortenedUrl(String shortUrl);

  List<UrlShortenerModel> findByOriginalUrlAndClientName(String originalUrl, String clientName);

}
