package org.magneton.core.access;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
@Setter
@Getter
@ToString
public class AccessConfig {

  private int lockTime;
  private int lockSize;
  private int lockErrorCount;
  private int errorRecordTime;
  private int errorRecordSize;
}
