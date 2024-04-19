/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.framework.design.context;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import lombok.extern.slf4j.Slf4j;
import org.magneton.framework.design.exception.ExtNotFoundException;
import org.magneton.framework.design.protocol.GeneralStrategyProtocolExecutor;
import org.magneton.framework.design.protocol.LocalFirstStrategyProtocolRegisterProcessor;
import org.magneton.framework.design.protocol.StrategyProtocolExecutor;
import org.magneton.framework.design.protocol.StrategyProtocolRegisterProcessor;
import org.magneton.framework.design.scenario.BizScenarioIdIterator;
import org.magneton.framework.design.scenario.DefaultBizScenarioIdIterator;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 策略内置执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class BosomStrategy {

	static {
		register(BizScenarioIdIterator.class, "*", new DefaultBizScenarioIdIterator());
		register(StrategyProtocolRegisterProcessor.class, "*", new LocalFirstStrategyProtocolRegisterProcessor());
		register(StrategyProtocolExecutor.class, "*", new GeneralStrategyProtocolExecutor());
		loadExtExecutor();
	}

	private BosomStrategy() {
		// private
	}

	public static void register(Class<?> clazz, String id, Object executor) {
		Strategies.register(genId(clazz, id), executor);
	}

	@Nullable
	public static <T> T unregister(Class<T> clazz, String id) {
		return (T) Strategies.unregister(genId(clazz, id));
	}

	public static <T> T get(Class<T> clazz, String id) {
		return (T) Strategies.get(genId(clazz, id));
	}

	public static <T> T getOrDefault(Class<T> clazz, String id) {
		Object strategy = Strategies.get(genId(clazz, id));
		if (strategy == null) {
			strategy = Strategies.get(genId(clazz, "*"));
			Verify.verifyNotNull(strategy, "not strategy found on %s:*", clazz.getName());
		}
		return (T) strategy;
	}

	/**
	 * 生成id
	 * @param clazz 类
	 * @param id id
	 * @param <T> 类型
	 * @return id
	 */
	private static <T> String genId(Class<T> clazz, String id) {
		return clazz.getName() + "#" + id;
	}

	/**
	 * 加载扩展执行器
	 */
	private static void loadExtExecutor() {
		ClassLoader classLoader = BosomStrategy.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream("gaia-strategy.properties")) {
			Properties properties = new Properties();
			properties.load(is);

			Enumeration<Object> keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				String value = properties.getProperty(key);
				if (Strings.isNullOrEmpty(value)) {
					continue;
				}
				Class<?> clazz = Class.forName(value, false, classLoader);
				Object strategy = ReflectUtil.newInstance(clazz);
				register(clazz, key, strategy);
				Class<?>[] interfaces = clazz.getInterfaces();
				for (Class<?> i : interfaces) {
					register(i, key, strategy);
				}
			}
		}
		catch (ClassNotFoundException e) {
			throw new ExtNotFoundException("load ext error", e);
		}
		catch (UtilException e) {
			log.error(String.format("load ExtStrategy.properties error. %s", e.getMessage()), e);
		}
		catch (Exception e) {
			log.info("load gaia-strategy.properties failure. using default strategy config.");
		}
	}

}