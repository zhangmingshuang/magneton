package org.magneton.test;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.magneton.test.annotation.TestComponentScan;
import org.magneton.test.core.ChaosApplication;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.core.Config;
import org.magneton.test.core.TraceChain;
import org.magneton.test.generate.TestValueGenerator;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.simplily.Printer;
import org.magneton.test.supplier.TestBooleanSupplier;

/**
 * .
 *
 * @author zhangmsh 2021/7/30
 * @since 2.0.0
 */
@Slf4j
@TestComponentScan
public final class ChaosTest implements ChaosApplication {

  private static final Config DEFAULT_CONFIG = new Config();

  static {
    ChaosContext.init(ChaosTest.class);
  }

  private ChaosTest() {}

  public static <T> T createByAngel(Class<T> clazz) {
    return createByAngel(clazz, DEFAULT_CONFIG);
  }

  public static <T> T createByAngel(Class<T> clazz, Config config) {
    return create(clazz, config, InjectType.ANGEL);
  }

  /**
   * 恶魔模式
   *
   * <p>该模式创建的类对象存在非常多的不确定性，通常用来进行混乱测试时使用。
   *
   * <p>该模式可能会返回 {@code null}， 该概率由 {@link Config#getDemonNullProbability()} 控制。
   *
   * <p>如果是一个对象，并且没有返回{@code null}，则对象的内部字段数据也有可能为 {@code null}，概率同上。
   *
   * <p>如果对象的字段注解了标准的 JSR303注解，则会根据该注释的意义进行反向注入。比如 {@code @NotEmpty}则一定会返回{@code null}
   *
   * <p>比如{@code @Min(1)}，则一定会返回小于1的数。
   *
   * @param clazz 要创建的类
   * @param <T> 泛型
   * @return 创建的类
   */
  @Nullable
  public static <T> T createByDemon(Class<T> clazz) {
    return createByDemon(clazz, DEFAULT_CONFIG);
  }

  /**
   * 恶魔模式
   *
   * <p>该模式创建的类对象存在非常多的不确定性，通常用来进行混乱测试时使用。
   *
   * <p>该模式可能会返回 {@code null}， 该概率由 {@link Config#getDemonNullProbability()} 控制。
   *
   * <p>如果是一个对象，并且没有返回{@code null}，则对象的内部字段数据也有可能为 {@code null}，概率同上。
   *
   * <p>如果对象的字段注解了标准的 JSR303注解，则会根据该注释的意义进行反向注入。比如 {@code @NotEmpty}则一定会返回{@code null}
   *
   * <p>比如{@code @Min(1)}，则一定会返回小于1的数。
   *
   * @param clazz 要创建的类
   * @param <T> 泛型
   * @return 创建的类
   */
  @Nullable
  public static <T> T createByDemon(Class<T> clazz, Config config) {
    return create(clazz, config, InjectType.DEMON);
  }

  public static <T> T create(Class<T> clazz, Config config, InjectType injectType) {
    TraceChain.current().start(clazz);
    try {
      return Objects.requireNonNull(ChaosContext.get(InjectorFactory.class))
          .inject(Optional.ofNullable(config).orElse(DEFAULT_CONFIG), Inject.of(clazz), injectType);
    } finally {
      TraceChain.current().end();
    }
  }

  public static TestBooleanSupplier booleanSupplier() {
    return TestBooleanSupplier.getInstance();
  }

  public static TestValueGenerator valueGenerator() {
    return TestValueGenerator.getInstance();
  }

  public static Printer printer() {
    return Printer.getInstance();
  }
}
