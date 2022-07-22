package org.magneton.module.pay.wechat.v3.prepay;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.prepay.entity.PrepayId;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayJSAPIPrepay;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayJSAPIPrepayReq;
import org.slf4j.Logger;

/**
 * JSAPI预支付.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Slf4j
public class JSAPIPrepayImpl extends AbstractPrepay implements JSAPIPrepay {

	public JSAPIPrepayImpl(WxPayContext wxPayContext) {
		super(wxPayContext);
	}

	@Override
	public Consequences<WxPayJSAPIPrepay> prepay(WxPayJSAPIPrepayReq req) {
		Preconditions.checkNotNull(req);
		Preconditions.checkNotNull(req.getOutTradeNo());
		Preconditions.checkNotNull(req.getDescription());
		if (Strings.isNullOrEmpty(req.getNotifyUrl())) {
			req.setNotifyUrl(Preconditions.checkNotNull(super.getPayConfig().getNotifyUrl(), "notifyUrl is empty"));
		}
		if (Strings.isNullOrEmpty(req.getAppId())) {
			req.setAppId(Preconditions.checkNotNull(this.getPayConfig().getAppId().getJsapi(),
					"app prepay appId must be not null"));
		}
		if (Strings.isNullOrEmpty(req.getMchId())) {
			req.setMchId(
					Preconditions.checkNotNull(this.getPayConfig().getMerchantId(), "merchantId must be not null"));
		}
		int amount = Preconditions.checkNotNull(req.getAmount()).getTotal();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		Consequences<PrepayId> prepayRes = this.doPreOrder("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi",
				req, PrepayId.class);
		if (!prepayRes.isSuccess()) {
			if (log.isDebugEnabled()) {
				log.debug("jsapi prepay:{} : ({})", req, prepayRes);
			}
			return prepayRes.coverage();
		}

		// 组成装APP预支付订单，用来提供给微信进行支付
		PrepayId prepay = Preconditions.checkNotNull(prepayRes.getData());

		WxPayJSAPIPrepay res = new WxPayJSAPIPrepay();
		res.setAppId(req.getAppId());
		res.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000L));

		String nonce = RandomUtil.randomString(32);
		res.setNonceStr(nonce);

		res.setPackageValue("prepay_id=" + prepay.getPrepayId());
		res.setSignType("RSA");

		String signStr = this.signStr(res);
		String sign = this.doSign(signStr);
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
		String signStr = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepayId + "\n";
		if (log.isDebugEnabled()) {
			log.debug("jsapi prepay sign str: {}", signStr);
		}
		return signStr;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
