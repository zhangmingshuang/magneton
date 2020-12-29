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
class EgoResponseMessageTest {

  @Test
  void message() {
    Response<Data> ok = Response.ok(new Data()).message("message");
    Assertions.assertEquals(
        "egoMessage", ok.getMessage(), "response's message not rewrite with ego");
  }

  class Data implements EgoResponseMessage {

    @Override
    public String message() {
      return "egoMessage";
    }
  }
}
