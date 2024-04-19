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

package org.magneton.framework.design;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.magneton.framework.design.context.BosomStrategy;
import org.magneton.framework.design.exception.ExtNotFoundException;
import org.magneton.framework.design.scenario.BizScenario;
import org.magneton.framework.design.scenario.BizScenarioIdIterator;

import java.util.Iterator;

/**
 * 扩展策略
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class StrategyExecutor {

	private StrategyExecutor() {
		// private
	}

	public static <T> T of(Class<T> clazz, BizScenario bizScenario) {
		Preconditions.checkNotNull(clazz, "clazz");

		BizScenarioIdIterator idIterator = BosomStrategy.getOrDefault(BizScenarioIdIterator.class, clazz.getName());
		Verify.verifyNotNull(idIterator, "not %s BizScenarioIdIterator found", clazz.getName());

		Iterator<String> iterator = idIterator.iterator(bizScenario);
		while (iterator.hasNext()) {
			String id = iterator.next();
			Object strategy = BosomStrategy.get(clazz, id);
			if (strategy == null) {
				continue;
			}
			return (T) strategy;
		}
		throw new ExtNotFoundException(String.format("not %s ExtStrategy found", clazz.getName()));
	}

}