package org.magneton.enhance.pay.core;

import org.magneton.enhance.pay.wxv3.core.WxPayTradeType;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * 支付类型.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public enum PayTypes {

	/**
	 * 微信支付.
	 */
	WECHAT(0, WxPayTradeType.values());

	private final int code;

	private final TradeType[] tradeTypes;

	PayTypes(int code, TradeType[] tradeTypes) {
		this.code = code;
		this.tradeTypes = tradeTypes;
	}

	public int getCode() {
		return this.code;
	}

	@Nullable
	public TradeType tradeTypeOf(String name) {
		for (TradeType tradeType : this.tradeTypes) {
			if (tradeType.isName(name.toUpperCase(Locale.ROOT))) {
				return tradeType;
			}
		}
		return null;
	}

}