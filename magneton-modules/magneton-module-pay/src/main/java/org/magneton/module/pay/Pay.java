package org.magneton.module.pay;

import cn.hutool.core.util.IdUtil;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
public interface Pay {

	default String outTradeNo() {
		return IdUtil.objectId();
	}

}
