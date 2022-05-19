package org.magneton.module.pay.wechat.v3.prepay;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.magneton.core.Consequences;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.wechat.v3.entity.WxPayH5PrepayReq;
import org.magneton.module.pay.wechat.v3.entity.WxPayH5PrepayRes;

/**
 * .
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
public class H5PrepayImpl implements H5Prepay {

	private final WechatBaseV3Pay basePay;

	public H5PrepayImpl(WechatBaseV3Pay basePay) {
		this.basePay = basePay;
	}

	@Override
	public Consequences<WxPayH5PrepayRes> prepay(WxPayH5PrepayReq req) {
		Preconditions.checkNotNull(req);
		Preconditions.checkNotNull(req.getOutTradeNo());
		Preconditions.checkNotNull(req.getDescription());
		int amount = Preconditions.checkNotNull(req.getAmount()).getTotal();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		Consequences<WxPayH5PrepayRes> prepayRes = this.basePay
				.doPreOrder("https://api.mch.weixin.qq.com/v3/pay/transactions/h5", req, WxPayH5PrepayRes.class);
		if (!prepayRes.isSuccess()) {
			return prepayRes.coverage();
		}
		return Consequences.success(prepayRes.getData());
	}

}
