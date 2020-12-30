package org.magneton.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.ResponseCodesSupplier.Codes;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
class ResponseCodesSupplierTest {

  private static final String CODE = "100";
  private static final String BAD = "bad";

  @Test
  void getInstance() {
    ResponseCodesSupplier responseCodesSupplier = ResponseCodesSupplier.getInstance();
    this.changeresponseCodes();
    ResponseCodesSupplier customizedresponseCodesSupplier = ResponseCodesSupplier.getInstance();
    Assertions.assertNotEquals(
        responseCodesSupplier,
        customizedresponseCodesSupplier,
        "response codes responseCodes change error");
  }

  @Test
  void ok() {
    ResponseCodesSupplier responseCodesSupplier = ResponseCodesSupplier.getInstance();
    ResponseMessage ok = responseCodesSupplier.ok();
    Assertions.assertEquals(Codes.OK, ok, "responseCodes ok error");
  }

  @Test
  void bad() {
    this.changeresponseCodes();
    ResponseCodesSupplier responseCodesSupplier = ResponseCodesSupplier.getInstance();
    ResponseMessage bad = responseCodesSupplier.bad();
    Assertions.assertEquals(CODE, bad.code(), "responseCodes code does not match");
    Assertions.assertEquals(BAD, bad.message(), "responseCodes message does not match");
  }

  @Test
  void exception() {
    ResponseCodesSupplier responseCodesSupplier = ResponseCodesSupplier.getInstance();
    ResponseMessage exception = responseCodesSupplier.exception();
    Assertions.assertEquals(Codes.EXCEPTION, exception, "responseCodes exception error");
  }

  private void changeresponseCodes() {
    ResponseCodesSupplier.Instance.setResponseCodesSupplier(
        new ResponseCodesSupplier() {
          @Override
          public ResponseMessage bad() {
            return ResponseMessage.valueOf(CODE, BAD);
          }
        });
  }
}
