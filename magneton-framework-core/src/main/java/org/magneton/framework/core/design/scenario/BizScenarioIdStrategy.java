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

package org.magneton.framework.core.design.scenario;

/**
 * ID策略
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class BizScenarioIdStrategy {

	private BizScenarioIdStrategy() {
		// private
	}

	public static class DefaultScenario implements BizScenarioId {

		@Override
		public String getId(BizScenario bizScenario) {
			return BizScenarioId.genId(bizScenario.getBizId(), bizScenario.getUseCase(), BizScenario.DEFAULT_SCENARIO);
		}

	}

	public static class DefaultUseCase implements BizScenarioId {

		@Override
		public String getId(BizScenario bizScenario) {
			return BizScenarioId.genId(bizScenario.getBizId(), BizScenario.DEFAULT_USE_CASE,
					BizScenario.DEFAULT_SCENARIO);
		}

	}

	public static class UniqueIdentity implements BizScenarioId {

		@Override
		public String getId(BizScenario bizScenario) {
			return BizScenarioId.genId(bizScenario.getBizId(), bizScenario.getUseCase(), bizScenario.getScenario());
		}

	}

	public static class DefaultAll implements BizScenarioId {

		@Override
		public String getId(BizScenario bizScenario) {
			return BizScenarioId.genId(BizScenario.DEFAULT_BIZ_ID, BizScenario.DEFAULT_USE_CASE,
					BizScenario.DEFAULT_SCENARIO);
		}

	}

}