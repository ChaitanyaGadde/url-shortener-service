package org.divya.url.shortener.model.db;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shortener_details")
public class UrlShortenerModel implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shortener_generator")
  @SequenceGenerator(name = "shortener_generator", sequenceName = "shortener_seq", allocationSize = 1)
  private int id;

  @Column(name = "client_name")
  private String clientName;

  @Column(name = "original_url")
  private String originalUrl;

  @Column(name = "shortened_url")
  private String shortenedUrl;

  @Column(name = "days_to_persist")
  private int daysToPersist;

  @Column(name = "created_date")
  @CreationTimestamp
  private OffsetDateTime createdDate;
}
