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

import com.google.auto.service.AutoService;
import org.magneton.framework.core.design.exception.MethodExecException;
import org.magneton.framework.core.spi.SPI;

import java.lang.reflect.InvocationTargetException;

/**
 * 本地执行协议
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@SPI
@AutoService(StrategyProtocol.class)
public class LocalStrategyProtocol extends AbstractStrategyProtocol {

	@Override
	public Object exec(StrategyContext context) {
		Object strategy = context.getStrategy();
		try {
			return context.getMethod().invoke(strategy, context.getArgs());
		}
		catch (InvocationTargetException | IllegalAccessException e) {
			throw new MethodExecException(e);
		}
	}

}