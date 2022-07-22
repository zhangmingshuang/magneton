package org.magneton.module.pay.wechat.v3.prepay;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.magneton.core.Consequences;
import org.magneton.foundation.exception.ProcessException;
import org.magneton.module.pay.wechat.v3.core.BaseV3Api;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayOrder;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayOrderQuery;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface Prepay<Req, Res> extends BaseV3Api {

	/**
	 * 预下单
	 * @param req 下单请求
	 * @return 下单结果
	 */
	Consequences<Res> prepay(Req req);

	/**
	 * 查询订单
	 * @param query 业务请求数据
	 * @return 订单数据
	 */
	default Consequences<WxPayOrder> queryOrder(WxPayOrderQuery query) {
		String merchantId = this.getPayConfig().getMerchantId();
		WxPayOrderQuery.Type reqIdType = query.getReqIdType();
		String url;
		switch (reqIdType) {
		case OUT_TRADE_NO:
			url = Strings.lenientFormat(
					"https://api.mch.weixin.qq.com/v3/pay/partner/transactions/out-trade-no/%s?mchid=%s",
					Preconditions.checkNotNull(query.getReqId()), merchantId);
			break;
		case TRANSACTION_ID:
			url = Strings.lenientFormat("https://api.mch.weixin.qq.com/v3/pay/transactions/id/%s?mchid=%s",
					Preconditions.checkNotNull(query.getReqId()), merchantId);
			break;
		default:
			throw new ProcessException("unknown reqIdType %s", reqIdType);
		}

		HttpGet httpGet = this.newHttpGet(url);

		Consequences<WxPayOrder> res = this.doRequest(httpGet, WxPayOrder.class);

		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		return res;

	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	default <T> Consequences<T> doPreOrder(String url, Object req, Class<T> clazz) {
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
