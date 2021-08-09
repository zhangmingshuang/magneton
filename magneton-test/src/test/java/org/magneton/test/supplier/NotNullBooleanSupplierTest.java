package org.magneton.test.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since
 */
class NotNullBooleanSupplierTest {

  @Test
  void getAsBoolean() {

    Assertions.assertFalse(new NotNullBooleanSupplier(null).print());

    NotNullBooleanSupplier notNullBooleanSupplier =
        new NotNullBooleanSupplier(new NotNullDto()).print();
    Assertions.assertTrue(notNullBooleanSupplier.getAsBoolean());

    notNullBooleanSupplier = new NotNullBooleanSupplier(new NullDto()).print();
    Assertions.assertFalse(notNullBooleanSupplier.getAsBoolean());
  }

  @Setter
  @Getter
  @ToString
  public static class NotNullDto {

    private int i;

    private Integer integer = 1;
  }

  @Setter
  @Getter
  @ToString
  public static class NullDto {

    private Integer integerValue;

    private Long longValue;
  }
}
