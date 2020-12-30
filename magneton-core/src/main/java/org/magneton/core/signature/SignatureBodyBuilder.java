package org.magneton.core.signature;

import java.util.Map;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public interface SignatureBodyBuilder {

  /**
   * generate signuration body content.
   *
   * @param body the generate body
   * @param salt the generate body salt
   * @return signuration body content
   */
  String build(Map<String, String> body, String salt);
}
