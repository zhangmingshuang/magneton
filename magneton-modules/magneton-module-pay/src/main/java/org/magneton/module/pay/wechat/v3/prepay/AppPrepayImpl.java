package org.magneton.module.pay.wechat.v3.prepay;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.prepay.entity.PrepayId;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayAppPrepay;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayAppPrepayReq;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class AppPrepayImpl extends AbstractPrepay implements AppPrepay {

	public AppPrepayImpl(WxPayContext wxPayContext) {
		super(wxPayContext);
	}

	@Override
	public Consequences<WxPayAppPrepay> prepay(WxPayAppPrepayReq req) {
		Preconditions.checkNotNull(req);
		Preconditions.checkNotNull(req.getOutTradeNo(), "outTradeNo must not be null");
		Preconditions.checkNotNull(req.getDescription(), "description must not be null");
		if (Strings.isNullOrEmpty(req.getNotifyUrl())) {
			req.setNotifyUrl(Preconditions.checkNotNull(this.getPayConfig().getNotifyUrl(), "notifyUrl is empty"));
		}
		if (Strings.isNullOrEmpty(req.getAppId())) {
			req.setAppId(Preconditions.checkNotNull(this.getPayConfig().getAppId().getApp(),
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
		Consequences<PrepayId> wechatV3PayPreOrderRes = this
				.doPreOrder("https://api.mch.weixin.qq.com/v3/pay/transactions/app", req, PrepayId.class);
		if (!wechatV3PayPreOrderRes.isSuccess()) {
			return wechatV3PayPreOrderRes.coverage();
		}
		// 组成装APP预支付订单，用来提供给微信进行支付
		PrepayId preOrder = Preconditions.checkNotNull(wechatV3PayPreOrderRes.getData());
		WxPayAppPrepay res = new WxPayAppPrepay();
		res.setAppId(req.getAppId());
		res.setPartnerId(
				Preconditions.checkNotNull(super.getPayConfig().getMerchantId(), "merchantId must not be null"));
		res.setPrepayId(Preconditions.checkNotNull(preOrder.getPrepayId(), "prepayId must not be null"));
		res.setPackageValue("Sign=WXPay");
		String nonce = RandomUtil.randomString(32);
		res.setNonceStr(nonce);
		res.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000L));

		String signStr = this.signStr(res);
		String sign = this.doSign(signStr);
		res.setSign(sign);
		return Consequences.success(res);
	}

	/**
	 * 签名 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_4.shtml
	 *
	 * <pre>
	 * 1、构造签名串
	 * 签名串一共有四行，每一行为一个参数。行尾以\n（换行符，ASCII编码值为0x0A）结束，包括最后一行。
	 * 如果参数本身以\n结束，也需要附加一个\n
	 *
	 * 参与签名字段及格式：
	 *
	 * 应用id
	 * 时间戳
	 * 随机字符串
	 * 预支付交易会话ID
	 *
	 * 数据举例：
	 *
	 * wx8888888888888888
	 * 1414561699
	 * 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * WX1217752501201407033233368018
	 * </pre>
	 * @param res
	 * @return 签名串
	 */
	private String signStr(WxPayAppPrepay res) {
		String appId = Preconditions.checkNotNull(res.getAppId());
		String timeStamp = Preconditions.checkNotNull(res.getTimeStamp());
		String nonceStr = Preconditions.checkNotNull(res.getNonceStr());
		String prepayId = Preconditions.checkNotNull(res.getPrepayId());
		String signStr = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepayId + "\n";
		if (log.isDebugEnabled()) {
			log.debug("wxPayAppPrepay sign str: {}", signStr);
		}
		return signStr;
	}

}
