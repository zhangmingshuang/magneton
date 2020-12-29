package org.magneton.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
class ResponseExceptionTest {

  @Test
  void valueOf() {
    ResponseException responseException =
        ResponseException.valueOf(Response.exception().message("error"));
    Assertions.assertEquals(
        "error", responseException.getMessage(), "exception message does not match");
  }

  @Test
  void getResponse() {
    Response exception = Response.exception();
    ResponseException responseException = ResponseException.valueOf(exception);
    Assertions.assertEquals(
        exception, responseException.getResponse(), "exception's response does not equals");
  }
}
