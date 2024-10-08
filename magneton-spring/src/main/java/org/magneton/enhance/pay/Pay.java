package org.magneton.enhance.pay;

import cn.hutool.core.util.IdUtil;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
public interface Pay {

	/**
	 * 外部订单号.
	 * @return 外部订单号
	 */
	default String outTradeNo() {
		return IdUtil.objectId();
	}

}