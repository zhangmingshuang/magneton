package org.magneton.enhance.pay.wxv3.profitsharing;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.wechat.pay.contrib.apache.httpclient.constant.WechatPayHttpHeaders;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.magneton.enhance.Result;
import org.magneton.enhance.pay.wxv3.core.WxPayContext;
import org.magneton.enhance.pay.wxv3.profitsharing.entity.*;
import org.slf4j.Logger;

import javax.crypto.IllegalBlockSizeException;

/**
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Slf4j
public class ProfitSharingImpl implements ProfitSharing {

	private final WxPayContext wxPayContext;

	public ProfitSharingImpl(WxPayContext wxPayContext) {
		this.wxPayContext = Preconditions.checkNotNull(wxPayContext);
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public Result<WxProfitSharingOrders> orders(WxProfitSharingOrdersReq req) {
		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/profitsharing/orders", req);
		return this.doRequest(httpPost, WxProfitSharingOrders.class);
	}

	@Override
	public Result<WxProfitSharingOrderState> state(WxProfitSharingOrderStateQuery query) {
		String outOrderNo = Preconditions.checkNotNull(query.getOutOrderNo(), "outOrderNo");
		String transactionId = Preconditions.checkNotNull(query.getTransactionId(), "transactionId");
		String url = String.format("https://api.mch.weixin.qq.com/v3/profitsharing/orders/%s?transaction_id=%s",
				outOrderNo, transactionId);
		HttpPost httpPost = this.newHttpPost(url, query);
		return this.doRequest(httpPost, WxProfitSharingOrderState.class);
	}

	@Override
	public Result<WxProfitSharingReceiverAdd> add(WxProfitSharingReceiverAddReq req) {
		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/profitsharing/receivers/add", req);
		String name = req.getName();
		if (!Strings.isNullOrEmpty(name)) {
			httpPost.addHeader(WechatPayHttpHeaders.WECHAT_PAY_SERIAL,
					this.getPayContext().getPayConfig().getMerchantSerialNumber());
			// name需要加密
			try {
				req.setName(RsaCryptoUtil.encryptOAEP(name, this.getPayContext().getVerifier().getValidCertificate()));
			}
			catch (IllegalBlockSizeException e) {
				throw new RuntimeException(e);
			}
		}
		return this.doRequest(httpPost, WxProfitSharingReceiverAdd.class);
	}

	@Override
	public WxPayContext getPayContext() {
		return this.wxPayContext;
	}

}
