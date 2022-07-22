package org.magneton.module.pay.wechat.v3.refund;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.magneton.core.Consequences;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.refund.entity.DomesticRefunds;
import org.magneton.module.pay.wechat.v3.refund.entity.DomesticRefundsReq;

/**
 * 退款
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
@Slf4j
public class RefundImpl implements Refund {

	private final WxPayContext wxPayContext;

	public RefundImpl(WxPayContext wxPayContext) {
		this.wxPayContext = Preconditions.checkNotNull(wxPayContext);
	}

	@Override
	public Consequences<DomesticRefunds> refund(DomesticRefundsReq req) {
		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds", req);
		return this.doRequest(httpPost, DomesticRefunds.class);
	}

	@Override
	public WxPayContext getPayContext() {
		return this.wxPayContext;
	}

}
