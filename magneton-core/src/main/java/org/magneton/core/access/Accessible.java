package org.magneton.core.access;

import lombok.Getter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
@Getter
@ToString
public class Accessible {

  /** {@code false} if the suppliter response is {@code false} or the key is locked. */
  private boolean access;
  /** differen from {@code access}, make it clear that it is locked. */
  private boolean locked;
  /** errors count remaining. */
  private int errorRemainCount;
  /** the lock time to live if locekd. */
  private long ttl;
  /** the refuse message. */
  private String refuseMessage;

  private Accessible() {}

  /**
   * access.
   *
   * @param access
   * @param errorRemainCount remaining count of errors.
   * @return accessible.
   */
  public static Accessible access(boolean access, int errorRemainCount) {
    Accessible accessible = new Accessible();
    accessible.access = access;
    accessible.errorRemainCount = errorRemainCount;
    return accessible;
  }

  /**
   * lock.
   *
   * @param ttl ttl of lock.
   * @param refuseMessage message of refuse.
   * @return accessible.
   */
  public static Accessible lock(long ttl, String refuseMessage) {
    Accessible accessible = new Accessible();
    accessible.locked = true;
    accessible.errorRemainCount = -1;
    accessible.ttl = ttl;
    accessible.refuseMessage = refuseMessage;
    return accessible;
  }
}
