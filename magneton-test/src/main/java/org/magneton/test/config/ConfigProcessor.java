package org.magneton.test.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Nullable;
import org.magneton.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/19
 * @since 2.0.0
 */
public interface ConfigProcessor {

  int nextSize(Config config, Definition definition);

  boolean nullable(Config config, Definition definition);

  @Nullable
  BigDecimal nextBigDecimal(Config config, Definition definition);

  @Nullable
  BigInteger nextBigInteger(Config config, Definition definition);

  @Nullable
  Byte nextByte(Config config, Definition definition);

  @Nullable
  Short nextShort(Config config, Definition definition);

  @Nullable
  Integer nextInt(Config config, Definition definition);

  @Nullable
  Long nextLong(Config config, Definition definition);

  @Nullable
  Float nextFloat(Config config, Definition definition);

  @Nullable
  Double nextDouble(Config config, Definition definition);

  @Nullable
  Boolean nextBoolean(Config config, Definition definition);

  @Nullable
  Character nextCharacter(Config config, Definition definition);

  @Nullable
  String nextString(Config config, Definition definition);

  @Nullable
  Date nextDate(Config config, Definition definition);
}
