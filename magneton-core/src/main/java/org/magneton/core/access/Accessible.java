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
  private boolean locked;
  /** errors count remaining. */
  private int errorRemainCount;

  private long ttl;
  private String refuseMessage;

  private Accessible() {}

  /**
   * access.
   *
   * @param errorRemainCount remaining count of errors.
   * @return accessible.
   */
  public static Accessible access(int errorRemainCount) {
    Accessible accessible = new Accessible();
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
