package cn.nascent.framework.test.injector;

import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.processor.AnnotationProcessorFactory;
import cn.nascent.framework.test.util.ConfigUtil;
import cn.nascent.framework.test.util.DemonUtil;
import java.lang.reflect.Array;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public abstract class AbstractInjector implements Injector {

  private AnnotationProcessorFactory annotationProcessorFactory;

  @Override
  @Nullable
  public Object inject(Config config, InjectType injectType, Inject inject) {
    return this.inject(config, injectType, inject, false);
  }

  @Override
  public Object injectRequired(Config config, InjectType injectType, Inject inject) {
    return this.inject(config, injectType, inject, true);
  }

  public Object inject(Config config, InjectType injectType, Inject inject, boolean requried) {
    if (inject.isArray()) {
      return this.arrayProcess(
          config,
          injectType,
          length -> this.createArray(config, injectType, inject, length),
          (i, componentType) -> this.processValue(config, injectType, componentType, requried));
    }
    return this.processValue(config, injectType, inject, requried);
  }

  private Object processValue(
      Config config, InjectType injectType, Inject inject, boolean required) {
    return this.annotationProcessorFactory.process(
        config, injectType, inject, required, () -> this.createValue(config, injectType, inject));
  }

  @Nullable
  protected abstract Object createValue(Config config, InjectType injectType, Inject inject);

  @Nullable
  protected abstract Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length);

  @Nullable
  protected Object arrayProcess(
      Config config,
      InjectType injectType,
      Function<Integer, Object> arrayCreator,
      BiFunction<Integer, Inject, Object> valueCreator) {
    int length = DemonUtil.createInt(injectType, ConfigUtil.getRandomSize(config));
    if (length == 0) {
      return null;
    }
    Object array = arrayCreator.apply(length);
    if (array == null) {
      return null;
    }
    Class<?> componentType = array.getClass().getComponentType();
    for (int i = 0; i < length; i++) {
      Object value = valueCreator.apply(i, Inject.of(componentType, array));
      if (value == null) {
        continue;
      }
      Array.set(array, i, value);
    }
    return array;
  }

  @Override
  public void setProcessorFactory(AnnotationProcessorFactory annotationProcessorFactory) {
    this.annotationProcessorFactory = annotationProcessorFactory;
  }
}
