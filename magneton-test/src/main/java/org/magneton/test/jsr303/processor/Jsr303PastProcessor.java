package org.magneton.test.jsr303.processor;

import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import org.magneton.test.annotation.TestComponent;

/**
 * {@code @PastOrPresent} 验证注解的元素值（日期类型）比当前时间早，或者是当前时间
 *
 * <p>{@code @Past} 验证注解的元素值（日期类型）比当前时间早
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@TestComponent
public class Jsr303PastProcessor extends AbstractJsr303DateProcessor {

  private static final int PLUS_DAY = -1;

  private static final int PLUS_MONTH = -1;

  private static final int PLUS_YEAR = -1;

  private static final int PLUS_HOUR_MILLIS = -3600 * 1000;

  @Override
  public boolean processable(Class annotationType) {
    return Past.class == annotationType || PastOrPresent.class == annotationType;
  }

  @Override
  protected int getMonth() {
    return PLUS_MONTH;
  }

  @Override
  protected int getYear() {
    return PLUS_YEAR;
  }

  @Override
  protected int getDay() {
    return PLUS_DAY;
  }

  @Override
  protected int getHourMills() {
    return PLUS_HOUR_MILLIS;
  }
}
