package org.divya.url.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.divya")
public class App {

  public static void main(String... args) {
    SpringApplication.run(App.class, args);
  }
}
