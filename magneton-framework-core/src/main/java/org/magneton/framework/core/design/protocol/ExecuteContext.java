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

package org.magneton.framework.core.design.protocol;

import cn.hutool.core.lang.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 策略协议执行上下文
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class ExecuteContext {

	private Object strategy;

	private Method method;

	private Object[] args;

	/**
	 * 执行过的协议链及响应的数据
	 */
	private List<Pair<StrategyProtocol, Object>> chain;

	public void addChain(StrategyProtocol strategyProtocol, Object value) {
		if (this.chain == null) {
			this.chain = new ArrayList<>();
		}
		this.chain.add(Pair.of(strategyProtocol, value));
	}

	public void chainForeach(BiConsumer<StrategyProtocol, Object> consumer) {
		if (this.chain == null || this.chain.isEmpty()) {
			return;
		}
		this.chain.forEach(pair -> consumer.accept(pair.getKey(), pair.getValue()));
	}

	@Nullable
	public Object lastResponse() {
		return this.chain == null ? null : this.chain.get(this.chain.size() - 1).getValue();
	}

}