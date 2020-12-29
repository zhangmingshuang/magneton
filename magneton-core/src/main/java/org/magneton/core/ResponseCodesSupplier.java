package org.magneton.core;

import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
@SuppressWarnings("MissingJavadocType")
public interface ResponseCodesSupplier {

  static ResponseCodesSupplier getInstance() {
    return Instance.responseCodesSupplier;
  }

  default ResponseMessage ok() {
    return Codes.OK;
  }

  default ResponseMessage bad() {
    return Codes.BAD;
  }

  default ResponseMessage exception() {
    return Codes.EXCEPTION;
  }

  /** default response messsage defined. */
  @ToString
  enum Codes implements ResponseMessage {
    /** success. */
    OK("0", "操作成功"),
    /** fail. */
    BAD("1", "操作失败"),
    /** exception. */
    EXCEPTION("2", "系统异常");

    private final String code;
    private final String message;

    Codes(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String code() {
      return this.code;
    }

    @Override
    public String message() {
      return this.message;
    }
  }

  /**
   * Response codes real supplier.
   *
   * <p>In default, the global reponse use the default code to reply. but it may change the default
   * rule at possible. {@code Instance} exposed a enterance to change the response's code supplier.
   */
  class Instance {
    private static ResponseCodesSupplier responseCodesSupplier = new ResponseCodesSupplier() {};

    private Instance() {}

    public static void setResponseCodesSupplier(ResponseCodesSupplier responseCodesSupplier) {
      Instance.responseCodesSupplier = responseCodesSupplier;
    }
  }
}
