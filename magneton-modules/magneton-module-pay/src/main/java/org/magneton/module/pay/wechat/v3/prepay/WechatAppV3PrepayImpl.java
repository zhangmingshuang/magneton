package org.magneton.module.pay.wechat.v3.prepay;

import cn.hutool.core.util.RandomUtil;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.wechat.v3.entity.WechatAppV3PayRes;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderReq;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderRes;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public class WechatAppV3PrepayImpl implements WechatAppV3Prepay {

	private final WechatBaseV3Pay wechatBaseV3Pay;

	public WechatAppV3PrepayImpl(WechatBaseV3Pay wechatBaseV3Pay) {
		this.wechatBaseV3Pay = wechatBaseV3Pay;
	}

	@Override
	public Consequences<WechatAppV3PayRes> preOrder(WechatV3PayPreOrderReq req) {
		Preconditions.checkNotNull(req);
		String outTradeNo = Preconditions.checkNotNull(req.getOutTradeNo());
		String description = Preconditions.checkNotNull(req.getDescription());
		int amount = Preconditions.checkNotNull(req.getAmount()).getTotal();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		Consequences<WechatV3PayPreOrderRes> wechatV3PayPreOrderRes = this.wechatBaseV3Pay.doPreOrder(req);
		if (!wechatV3PayPreOrderRes.isSuccess()) {
			return wechatV3PayPreOrderRes.coverage();
		}
		WechatV3PayPreOrderRes preOrder = Preconditions.checkNotNull(wechatV3PayPreOrderRes.getData());
		WechatAppV3PayRes res = new WechatAppV3PayRes();
		res.setAppId(this.wechatBaseV3Pay.getPayConfig().getAppId());
		res.setPartnerId(this.wechatBaseV3Pay.getPayConfig().getMerchantId());
		res.setPrepayId(preOrder.getPrepayId());
		res.setPackageValue("Sign=WXPay");
		String nonce = RandomUtil.randomString(32);
		res.setNonceStr(nonce);
		res.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000L));

		String signStr = this.signStr(res);
		String sign = this.wechatBaseV3Pay.doSign(signStr);
		res.setSign(sign);
		return Consequences.success(res);
	}

	/**
	 * 签名
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
	private String signStr(WechatAppV3PayRes res) {
		String appId = Preconditions.checkNotNull(res.getAppId());
		String timeStamp = Preconditions.checkNotNull(res.getTimeStamp());
		String nonceStr = Preconditions.checkNotNull(res.getNonceStr());
		String prepayId = Preconditions.checkNotNull(res.getPrepayId());
		return appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepayId;
	}

}
