package org.magneton.module.pay.wechat.v3.prepay;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.magneton.core.Consequences;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.wechat.v3.entity.PrepayId;
import org.magneton.module.pay.wechat.v3.entity.WxPayJSAPIPrepay;
import org.magneton.module.pay.wechat.v3.entity.WxPayJSAPIPrepayReq;

/**
 * JSAPI预支付.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
public class JSAPIPrepayImpl implements JSAPIPrepay {

	private final WechatBaseV3Pay basePay;

	public JSAPIPrepayImpl(WechatBaseV3Pay basePay) {
		this.basePay = basePay;
	}

	@Override
	public Consequences<WxPayJSAPIPrepay> prepay(WxPayJSAPIPrepayReq req) {
		Preconditions.checkNotNull(req);
		Preconditions.checkNotNull(req.getOutTradeNo());
		Preconditions.checkNotNull(req.getDescription());
		int amount = Preconditions.checkNotNull(req.getAmount()).getTotal();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		Consequences<PrepayId> prepayRes = this.basePay
				.doPreOrder("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi", req, PrepayId.class);
		if (!prepayRes.isSuccess()) {
			return prepayRes.coverage();
		}

		// 组成装APP预支付订单，用来提供给微信进行支付
		PrepayId prepay = Preconditions.checkNotNull(prepayRes.getData());
		WxPayJSAPIPrepay res = new WxPayJSAPIPrepay();
		res.setAppId(this.basePay.getPayContext().getPayConfig().getAppId());
		res.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000L));

		String nonce = RandomUtil.randomString(32);
		res.setNonceStr(nonce);

		res.setPackageValue(prepay.getPrepayId());
		res.setSignType("RSA");

		String signStr = this.signStr(res);
		String sign = this.basePay.doSign(signStr);
		res.setPaySign(sign);
		return Consequences.success(res);
	}

	/**
	 * 生成签名字符串.
	 *
	 * 签名串一共有四行，每一行为一个参数。行尾以\n（换行符，ASCII编码值为0x0A）结束，包括最后一行。 如果参数本身以\n结束，也需要附加一个\n <pre>
	 * 应用ID
	 * 时间戳
	 * 随机字符串
	 * 订单详情扩展字符串
	 * </pre> https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_4.shtml
	 * @param res 预支付订单数据
	 * @return 签名字符串
	 */
	private String signStr(WxPayJSAPIPrepay res) {
		String appId = Preconditions.checkNotNull(res.getAppId());
		String timeStamp = Preconditions.checkNotNull(res.getTimeStamp());
		String nonceStr = Preconditions.checkNotNull(res.getNonceStr());
		String prepayId = Preconditions.checkNotNull(res.getPackageValue());
		return appId + "\n" + timeStamp + "\n" + nonceStr + "\nprepay_id=" + prepayId;
	}

}
