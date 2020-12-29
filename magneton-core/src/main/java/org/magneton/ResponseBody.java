package org.magneton;

import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/19
 */
public interface ResponseBody {

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

  @ToString
  @SuppressWarnings("MissingJavadocType")
  enum Codes implements ResponseBody {
    /** success. */
    SUCCESS("0", "操作成功"),
    /** fail. */
    FAIL("1", "操作失败"),
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
}
