package org.magneton.core.exception;

/**
 * 创建目录异常.
 *
 * @author zhangmsh
 * @since 2021/9/7
 */
public class MkdirException extends RuntimeException {
  public MkdirException(String message) {
    super(message);
  }
}
