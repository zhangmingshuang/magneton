package org.magneton.module.pay.wechat.v3.profitsharing;

import com.google.common.base.Preconditions;
import org.apache.http.client.methods.HttpPost;
import org.magneton.core.Consequences;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingOrderState;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingOrderStateQuery;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingOrders;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingOrdersReq;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingReceiverAdd;
import org.magneton.module.pay.wechat.v3.profitsharing.entity.WxProfitSharingReceiverAddReq;

/**
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
public class ProfitSharingImpl implements ProfitSharing {

	private final WxPayContext wxPayContext;

	public ProfitSharingImpl(WxPayContext wxPayContext) {
		this.wxPayContext = Preconditions.checkNotNull(wxPayContext);
	}

	@Override
	public Consequences<WxProfitSharingOrders> orders(WxProfitSharingOrdersReq req) {
		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/profitsharing/orders", req);
		return this.doRequest(httpPost, WxProfitSharingOrders.class);
	}

	@Override
	public Consequences<WxProfitSharingOrderState> state(WxProfitSharingOrderStateQuery query) {
		String outOrderNo = Preconditions.checkNotNull(query.getOutOrderNo(), "outOrderNo");
		String transactionId = Preconditions.checkNotNull(query.getTransactionId(), "transactionId");
		String url = String.format("https://api.mch.weixin.qq.com/v3/profitsharing/orders/%s?transaction_id=%s",
				outOrderNo, transactionId);
		HttpPost httpPost = this.newHttpPost(url, query);
		return this.doRequest(httpPost, WxProfitSharingOrderState.class);
	}

	@Override
	public Consequences<WxProfitSharingReceiverAdd> add(WxProfitSharingReceiverAddReq req) {
		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/profitsharing/receiver/add", req);
		return this.doRequest(httpPost, WxProfitSharingReceiverAdd.class);
	}

	@Override
	public WxPayContext getPayContext() {
		return this.wxPayContext;
	}

}
