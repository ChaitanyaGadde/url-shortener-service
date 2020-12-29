package org.divya.url.shortener.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class UrlHashGenerator {

  private MessageDigest digest;

  UrlHashGenerator(MessageDigest messageDigest) {
    this.digest = messageDigest;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public synchronized String generateToken(String uniqueParamsCombination) throws NoSuchAlgorithmException {
    String sha3256hex = bytesToHex(getHashBytes(uniqueParamsCombination));
    log.debug("token generator method generated token {} , converted params {}", sha3256hex, uniqueParamsCombination);
    return sha3256hex;
  }

  private byte[] getHashBytes(String toBeHashed) throws NoSuchAlgorithmException {
    digest = MessageDigest.getInstance("SHA3-256");
    return digest.digest(
        toBeHashed.getBytes(StandardCharsets.UTF_8));
  }
}
