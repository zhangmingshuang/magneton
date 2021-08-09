package cn.nascent.framework.test.injector.collection;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.annotation.TestSort;
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
import com.google.common.collect.Maps;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestSort
@TestComponent
@Slf4j
public class MapInjector extends AbstractInjector {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		Inject keyInject = InjectUtil.getOrElse(inject, 0, PrimitiveUtil.random());
		Inject valueInject = InjectUtil.getOrElse(inject, 1, PrimitiveUtil.random());
		Map map;
		if (inject.isInterface()) {
			if (SortedMap.class.isAssignableFrom(inject.getInectType())) {
				map = Maps.newTreeMap();
			}
			else if (Map.class.isAssignableFrom(inject.getInectType())) {
				map = Maps.newHashMap();
			}
			else {
				log.warn("不支持的Map类型{}", inject);
				return null;
			}
		}
		else {
			try {
				map = (Map) inject.getInectType().getConstructor().newInstance();
			}
			catch (InstantiationException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}

		int size = DemonUtil.createInt(injectType, ConfigUtil.getRandomSize(config));
		if (size == 0) {
			return null;
		}

		for (int i = 0; i < size; i++) {
			Object keyValue = this.injectorFactory.inject(config, keyInject, injectType);
			Object valueValue = this.injectorFactory.inject(config, valueInject, injectType);
			if (keyValue != null) {
				map.put(keyValue, valueValue);
			}
		}

		return map;
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (SortedMap[].class.isAssignableFrom(inject.getInectType())) {
			return new TreeMap[length];
		}
		else if (Map[].class.isAssignableFrom(inject.getInectType())) {
			return new HashMap[length];
		}
		throw new UnsupportedOperationException(Strings.lenientFormat("不支持的Map数组类型%s", inject.getInectType()));
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Map.class, Map[].class };
	}

}
