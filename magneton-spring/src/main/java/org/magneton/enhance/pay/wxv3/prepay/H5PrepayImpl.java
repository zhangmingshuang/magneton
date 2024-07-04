package org.magneton.enhance.pay.wxv3.prepay;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.magneton.enhance.Result;
import org.magneton.enhance.pay.exception.AmountException;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayH5Prepay;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayH5PrepayReq;

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
	public Result<WxPayH5Prepay> prepay(WxPayH5PrepayReq req) {
		Preconditions.checkNotNull(req);
		Preconditions.checkNotNull(req.getOutTradeNo());
		Preconditions.checkNotNull(req.getDescription());
		int amount = Preconditions.checkNotNull(req.getAmount()).getTotal();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		return this.basePay.doPreOrder("https://api.mch.weixin.qq.com/v3/pay/transactions/h5", req,
				WxPayH5Prepay.class);
	}

}
