package cn.nascent.framework.test.injector.collection;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.InjectorFactory;
import cn.nascent.framework.test.util.ConfigUtil;
import cn.nascent.framework.test.util.DemonUtil;
import cn.nascent.framework.test.util.InjectUtil;
import cn.nascent.framework.test.util.PrimitiveUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
@Slf4j
public class CollectionInjector extends AbstractInjector {

  @TestAutowired private InjectorFactory injectorFactory;

  @SuppressWarnings("OverlyBroadCatchBlock")
  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    Collection collection;

    Inject genericClass = InjectUtil.getOrElse(inject, 0, PrimitiveUtil.random());
    if (inject.isInterface()) {
      if (Set.class.isAssignableFrom(inject.getInectType())) {
        collection = Sets.newHashSet();
      } else if (Collection.class.isAssignableFrom(inject.getInectType())
          || List.class.isAssignableFrom(inject.getInectType())) {
        collection = Lists.newArrayList();
      } else {
        log.warn("不支持的集合类型{}", inject.getInectType());
        return null;
      }
    } else {
      try {
        collection = (Collection) inject.getInectType().getConstructor().newInstance();
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
    int size = DemonUtil.createInt(injectType, ConfigUtil.getRandomSize(config));
    if (size == 0) {
      return null;
    }

    for (int i = 0; i < size; i++) {
      Object value = this.injectorFactory.inject(config, genericClass, injectType);
      collection.add(value);
    }

    return collection;
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {

    if (Collection[].class.isAssignableFrom(inject.getInectType())
        || List[].class.isAssignableFrom(inject.getInectType())) {
      return new ArrayList[length];
    } else if (Set[].class.isAssignableFrom(inject.getInectType())) {
      return new HashSet[length];
    }
    throw new UnsupportedOperationException(
        Strings.lenientFormat("不支持的集合类型%s", inject.getInectType()));
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {Collection.class, Collection[].class};
  }
}
