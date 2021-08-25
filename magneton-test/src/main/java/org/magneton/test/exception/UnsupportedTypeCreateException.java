package org.magneton.test.exception;

import com.google.common.base.Strings;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public class UnsupportedTypeCreateException extends RuntimeException {

  private static final long serialVersionUID = -2070524012863299399L;

  public UnsupportedTypeCreateException(String message) {
    super(message);
  }

  public UnsupportedTypeCreateException(String message, Object... args) {
    this(Strings.lenientFormat(message, args));
  }

  public UnsupportedTypeCreateException(Throwable e) {
    super(e);
  }
}
