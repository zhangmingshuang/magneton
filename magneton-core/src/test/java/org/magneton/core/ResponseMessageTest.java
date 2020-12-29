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
class ResponseMessageTest {

  @Test
  void code() {
    Response response = Response.response(new CustomizedResponseMessage());
    Assertions.assertEquals("100", response.getCode(), "response code not equals");
  }

  @Test
  void message() {
    Response response = Response.response(new CustomizedResponseMessage());
    Assertions.assertEquals(
        "customized error", response.getMessage(), "response message not equals");
  }

  class CustomizedResponseMessage implements ResponseMessage {

    @Override
    public String code() {
      return "100";
    }

    @Override
    public String message() {
      return "customized error";
    }
  }
}
