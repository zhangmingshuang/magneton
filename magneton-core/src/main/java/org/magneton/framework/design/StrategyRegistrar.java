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
import org.magneton.framework.design.context.BosomStrategy;
import org.magneton.framework.design.protocol.StrategyProtocolExecutor;
import org.magneton.framework.design.scenario.BizScenario;
import org.magneton.framework.design.scenario.BizScenarioId;

import javax.annotation.Nullable;

/**
 * 策略注册器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class StrategyRegistrar {

	private StrategyRegistrar() {
		// private
	}

	/**
	 * 注册策略
	 * @param clazz 策略类型
	 * @param strategy 策略实例，需要注意策略实例必须有提供一个空构造器，否则无法实例化
	 * @param bizScenario 业务场景
	 * @param <T> T
	 */
	public static <T> void register(Class<T> clazz, T strategy, BizScenario bizScenario) {
		Preconditions.checkNotNull(clazz, "clazz");
		Preconditions.checkNotNull(strategy, "strategy");
		Preconditions.checkNotNull(bizScenario, "bizScenario");

		StrategyProtocolExecutor strategyProtocolExecutor = BosomStrategy.getOrDefault(StrategyProtocolExecutor.class,
				strategy.getClass().getName());
		String strategyId = BizScenarioId.genId(bizScenario.getBizId(), bizScenario.getUseCase(),
				bizScenario.getScenario());
		BosomStrategy.register(clazz, strategyId, strategyProtocolExecutor.convert(strategy));
	}

	@Nullable
	public static <T> T unregister(Class<T> clazz, BizScenario bizScenario) {
		Preconditions.checkNotNull(clazz, "clazz");
		Preconditions.checkNotNull(bizScenario, "bizScenario");

		String strategyId = BizScenarioId.genId(bizScenario.getBizId(), bizScenario.getUseCase(),
				bizScenario.getScenario());
		return BosomStrategy.unregister(clazz, strategyId);
	}

}