package org.magneton.test.injector;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.core.AfterAutowrited;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@Slf4j
@TestComponent
public class InjectorFactory implements AfterAutowrited {

  @TestAutowired private List<Injector> injectors;

  private final Map<Class, Injector> injectorTypeRef = Maps.newHashMap();

  public static InjectorFactory getInstance() {
    InjectorFactory injectorFactory = ChaosContext.getComponent(InjectorFactory.class);
    Verify.verifyNotNull(injectorFactory, "not injector factory found");
    return injectorFactory;
  }

  @Nullable
  public <T> T inject(Definition definition, Config config, InjectType injectType) {
    Object object =
        Preconditions.checkNotNull(this.getInjector(definition.getClazz()))
            .inject(definition, config, injectType);
    this.injectToObject(object, definition.getChildDefinitions(), config, injectType);
    return (T) object;
  }

  private void injectToObject(
      Object object, List<Definition> childDefinitions, Config config, InjectType injectType) {
    if (childDefinitions != null && !childDefinitions.isEmpty()) {
      for (Definition childDefinition : childDefinitions) {
        Class childClazz = childDefinition.getClazz();
        Object childObject =
            Preconditions.checkNotNull(this.getInjector(childClazz))
                .inject(childDefinition, config, injectType);
        if (childObject == null) {
          continue;
        }
        Field field = childDefinition.getField();
        if (field == null
            || Modifier.isStatic(field.getModifiers())
            || Modifier.isFinal(field.getModifiers())) {
          continue;
        }
        field.setAccessible(true);
        try {
          field.set(object, childObject);
          this.injectToObject(
              childObject, childDefinition.getChildDefinitions(), config, injectType);
        } catch (IllegalAccessException e) {
          log.error("set value:{} to {}#{} error", childObject, childClazz, field);
          log.error("set field value error", e);
        }
      }
    }
  }

  protected Injector getInjector(Class clazz) {
    Preconditions.checkNotNull(clazz, "clazz must not null");
    Injector injector = this.injectorTypeRef.get(clazz);
    if (injector != null) {
      return injector;
    }
    Set<Entry<Class, Injector>> entries = this.injectorTypeRef.entrySet();
    for (Entry<Class, Injector> entry : entries) {
      if (entry.getKey().isAssignableFrom(clazz)) {
        injector = entry.getValue();
        // fix ObjectInjector 会优先处理其他的类型导致处理错误
        if (injector != null && injector.getClass() != ObjectInjector.class) {
          if (injector.afterTypes().length > 0) {
            for (Class afterClass : injector.afterTypes()) {
              if (afterClass.isAssignableFrom(clazz)) {
                injector = this.injectorTypeRef.get(afterClass);
                if (injector != null) {
                  return injector;
                }
              }
            }
          }
          return injector;
        }
      }
    }
    return this.injectorTypeRef.get(Object.class);
  }

  @Override
  public void afterAutowrited() {
    Verify.verifyNotNull(this.injectors, "没有找到对应的注射器");
    for (Injector injector : this.injectors) {
      Class[] types = injector.getTypes();
      if (types == null || types.length < 1) {
        continue;
      }
      for (Class type : types) {
        this.injectorTypeRef.put(type, injector);
      }
    }
  }
}
