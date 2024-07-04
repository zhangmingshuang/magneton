package org.magneton.enhance.pay.wxv3.prepay;

import org.magneton.enhance.pay.wxv3.core.BaseV3Api;
import org.magneton.enhance.pay.wxv3.core.WxPayContext;

/**
 * @author zhangmsh 2022/7/15
 * @since 1.0.1
 */
public abstract class AbstractPrepay implements BaseV3Api {

	private final WxPayContext wxPayContext;

	public AbstractPrepay(WxPayContext wxPayContext) {
		this.wxPayContext = wxPayContext;
	}

	@Override
	public WxPayContext getPayContext() {
		return this.wxPayContext;
	}

}
