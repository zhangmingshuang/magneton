package org.magneton.module.pay.wechat.v3.prepay;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.magneton.core.Consequences;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.slf4j.Logger;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class AbstractWechatV3Pay implements WechatBaseV3Pay {

	private final WxPayContext payContext;

	public AbstractWechatV3Pay(WxPayContext payContext) {
		this.payContext = payContext;
	}

	@Nullable
	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public WxPayContext getPayContext() {
		return this.payContext;
	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	@Override
	public <T> Consequences<T> doPreOrder(String url, Object req, Class<T> clazz) {
		Preconditions.checkNotNull(req);

		HttpPost httpPost = this.newHttpPost(url, req);
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		Consequences<T> res = this.doRequest(httpPost, clazz);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		T prepayData = res.getData();
		return Consequences.success(prepayData);
	}

}
