package org.magneton.core.signature;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public class Sha1Signature extends AbstractSignature {

  /**
   * sha signature.
   *
   * @param salt signature content salt
   */
  public Sha1Signature(String salt) {
    super(salt);
  }

  @SuppressWarnings("java:S1874")
  @Override
  protected String generateSignature(String signatureContent) {
    //noinspection deprecation
    return Hashing.sha1().hashString(signatureContent, StandardCharsets.UTF_8).toString();
  }
}
