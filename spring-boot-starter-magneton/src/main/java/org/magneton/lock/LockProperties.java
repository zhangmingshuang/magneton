package org.magneton.lock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
@Setter
@Getter
@ToString
public class LockProperties {

  public static final String REDIS = "redis";
  public static final String TYPE = "type";
  /**
   * lock type. default is {@code redis}.
   *
   * <p>support list:
   *
   * <ul>
   *   <li>redis(default)
   * </ul>
   */
  private String type = "redis";
}
