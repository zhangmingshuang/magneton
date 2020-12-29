package org.magneton;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;
import org.magneton.ResponseBody.Codes;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/19
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue"})
@Getter
@ToString
public class Response<T> {
  private String code;
  private T data;
  private String message;
  private long timestamp;
  private Map<String, String> additions;

  private Response() {
    // private
  }

  public static Response ok() {
    return ok(null);
  }

  public static <T> Response<T> ok(@Nullable T t) {
    return response(Codes.SUCCESS, t);
  }

  public static Response bad() {
    return bad(null);
  }

  public static <T> Response<T> bad(T t) {
    return response(Codes.FAIL, t);
  }

  public static Response exception() {
    return response(Codes.EXCEPTION, null);
  }

  public static Response response(ResponseBody responseBody) {
    return response(responseBody, null);
  }

  /**
   * response with body.
   *
   * @param responseBody {@code ResponseBody}
   * @param data response data.
   * @param <T> T
   * @return {@code Response} of response.
   */
  public static <T> Response<T> response(ResponseBody responseBody, T data) {
    Response<T> response = new Response<>();
    String message = responseBody.message();
    if (data instanceof EgoResponseMessage) {
      message = ((EgoResponseMessage) data).message();
    }
    return response
        .code(responseBody.code())
        .message(message)
        .data(data)
        .timestamp(System.currentTimeMillis());
  }

  public Response<T> code(String code) {
    this.code = code;
    return this;
  }

  public Response<T> message(String message) {
    this.message = message;
    return this;
  }

  public Response<T> data(T data) {
    this.data = data;
    return this;
  }

  public Response<T> timestamp(long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * add a message entry.
   *
   * @param key addition message key.
   * @param value addition message value.
   * @return {@code Response} of response.
   */
  public Response<T> addition(String key, String value) {
    if (Objects.isNull(this.additions)) {
      this.additions = Maps.newHashMapWithExpectedSize(2);
    }
    this.additions.put(key, value);
    return this;
  }

  public boolean isOk() {
    return Codes.SUCCESS.code().equals(this.code);
  }

  public boolean isException() {
    return Codes.EXCEPTION.code().equals(this.code);
  }
}
