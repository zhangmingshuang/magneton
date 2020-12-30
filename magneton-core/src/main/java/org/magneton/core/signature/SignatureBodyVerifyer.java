package org.magneton.core.signature;

import java.util.Map;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public interface SignatureBodyVerifyer {

  /**
   * validate the signature body is expectations.
   *
   * @param body the given body to signature.
   * @throws SignatureBodyException if the given body is not in line with expectations.
   */
  void validate(Map<String, String> body) throws SignatureBodyException;
}
