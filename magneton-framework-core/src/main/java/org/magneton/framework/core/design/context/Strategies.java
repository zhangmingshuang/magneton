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

package org.magneton.framework.core.design.context;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.framework.core.design.exception.DuplicateStrategyException;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的策略执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class Strategies {

	private static final Map<String, Object> DATA = new ConcurrentHashMap<>();

	private Strategies() {
		// private
	}

	/**
	 * 注册策略
	 * @param id 策略ID
	 * @param strategy 策略实现
	 */
	public static void register(String id, Object strategy) {
		Preconditions.checkNotNull(id, "id");
		Preconditions.checkNotNull(strategy, "strategy");

		Object old = DATA.put(id, strategy);
		if (old != null) {
			throw new DuplicateStrategyException(
					String.format("duplicate ext strategy on %s, and %s with the same id: %s", strategy.getClass(),
							old.getClass(), id));
		}
		log.info("register strategy. id:{}, strategy:{}", id, strategy.getClass());
	}

	/**
	 * 注销策略
	 * @param id 策略ID
	 * @return 注销的策略，如果要注销的策略不存在，则返回 {@code null}
	 */
	@Nullable
	public static Object unregister(String id) {
		Preconditions.checkNotNull(id, "id");

		Object old = DATA.remove(id);
		if (old != null) {
			log.info("unregister strategy. id:{}, strategy:{}", id, old.getClass());
		}
		return old;
	}

	@Nullable
	public static Object get(String id) {
		Preconditions.checkNotNull(id, "id");

		return DATA.get(id);
	}

}