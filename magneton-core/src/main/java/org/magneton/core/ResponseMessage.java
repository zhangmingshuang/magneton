package org.magneton.core;

import com.google.common.base.Preconditions;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/19
 */
public interface ResponseMessage {

  /**
   * simply to create a {@code ResponseBody}.
   *
   * @param code response code.
   * @param message response message.
   * @return response body.
   */
  static ResponseMessage valueOf(String code, String message) {
    Preconditions.checkNotNull(code, "code must be not null");
    Preconditions.checkNotNull(message, "message must be not null");

    return new ResponseMessage() {
      @Override
      public String code() {
        return code;
      }

      @Override
      public String message() {
        return message;
      }
    };
  }

  /**
   * response code.
   *
   * @return code.
   */
  String code();

  /**
   * response message.
   *
   * @return message.
   */
  String message();
}
