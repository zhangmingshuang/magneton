package org.magneton.enhance.pay.wxv3.prepay;

import org.magneton.enhance.Result;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface Prepay<Req, Res> {

	/**
	 * 预下单
	 * @param req 下单请求
	 * @return 下单结果
	 */
	Result<Res> prepay(Req req);

}
