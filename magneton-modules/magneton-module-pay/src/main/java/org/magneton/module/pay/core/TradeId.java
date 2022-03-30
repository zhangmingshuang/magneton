package org.magneton.module.pay.core;

import cn.hutool.core.util.IdUtil;

/**
 * @author zhangmsh 2022/3/30
 * @since 1.0.0
 */
public class TradeId {

	private TradeId() {
	}

	public static String tradeNo() {
		return IdUtil.objectId();
	}

}
