package org.magneton.core;

/**
 * tag a response object supports providing its own message.
 *
 * <p>e.g: {@code Response.ok(Obj)}, if {@code Obj} instance of {@link EgoResponseMessage}, the
 * {@code Response#message()} will filling with {@link EgoResponseMessage#message()} automatic.
 *
 * <p>note: {@code Response.of(Obj).message(Msg)} will rewrite the message with {@code Msg}.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/30
 */
public interface EgoResponseMessage {

  /**
   * message to return.
   *
   * @return message body.
   */
  String message();
}
