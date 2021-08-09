package org.magneton.test.injector.object;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.annotation.TestSort;
import org.magneton.test.core.Config;
import org.magneton.test.core.TraceChain;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.util.PrimitiveUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestSort
@TestComponent
@Slf4j
public class ObjectInjector extends AbstractInjector {

  @TestAutowired private InjectorFactory injectorFactory;

  @SuppressWarnings({"ForLoopReplaceableByForEach", "OverlyBroadCatchBlock"})
  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    if (inject.getInectType() == Object.class) {
      return null;
    }
    Set<Field> fields = inject.getFields();
    Field field = null;
    Object value = null;
    try {
      boolean hadNotFields = fields.size() < 1;
      if (hadNotFields && inject.isPrimitive()) {
        return this.injectorFactory.inject(config, Inject.of(PrimitiveUtil.random()), injectType);
      }
      Object obj = inject.getInectType().getConstructor().newInstance();
      if (hadNotFields) {
        return obj;
      }
      for (Field f : fields) {
        field = f;
        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
          continue;
        }
        Class<?> fieldType = field.getType();
        value = this.injectorFactory.inject(config, Inject.of(fieldType, field), injectType);
        field.setAccessible(true);
        field.set(obj, value);
      }
      return obj;
    } catch (Throwable e) {
      log.error(
          "{}#{}设置值{}错误,injector:{}",
          inject.getInectType(),
          field,
          value,
          TraceChain.current().getCurrentInjector());
      throw new RuntimeException(e);
    }
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    Class componentType = inject.getInectType().getComponentType();
    return Array.newInstance(componentType, length);
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {Object.class};
  }
}
