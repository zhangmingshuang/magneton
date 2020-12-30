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

  void validate(Map<String, String> body) throws SignatureBodyException;
}
