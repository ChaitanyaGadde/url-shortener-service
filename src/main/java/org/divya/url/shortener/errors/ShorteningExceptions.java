package org.divya.url.shortener.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShorteningExceptions extends RuntimeException {

  private final HttpStatus status;
  private final String code;
  private final String reason;

  public ShorteningExceptions(HttpStatus status, String code, String reason, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
    this.code = code;
    this.reason = reason;
  }

  public ShorteningExceptions(HttpStatus status, String code, String reason, String message) {
    super(message);
    this.status = status;
    this.code = code;
    this.reason = reason;
  }

  public ShorteningExceptions(HttpStatus status, String reason, String message) {
    this(status, status.name(), reason, message);
  }

  public ShorteningExceptions(HttpStatus status, String message) {
    this(status, status.name(), status.getReasonPhrase(), message);
  }

  public ShorteningExceptions(HttpStatus status, String message, Throwable cause) {
    this(status, status.name(), status.getReasonPhrase(), message, cause);
  }
}
