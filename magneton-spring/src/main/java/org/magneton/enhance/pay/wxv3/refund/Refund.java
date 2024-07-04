package org.magneton.enhance.pay.wxv3.refund;

import org.magneton.enhance.Result;
import org.magneton.enhance.pay.wxv3.core.BaseV3Api;
import org.magneton.enhance.pay.wxv3.refund.entity.DomesticRefunds;
import org.magneton.enhance.pay.wxv3.refund.entity.DomesticRefundsReq;

/**
 * 微信退款
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_9.shtml
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
public interface Refund extends BaseV3Api {

	/**
	 * 退款
	 * @param req 请求
	 * @return 退款数据
	 */
	Result<DomesticRefunds> refund(DomesticRefundsReq req);

}
