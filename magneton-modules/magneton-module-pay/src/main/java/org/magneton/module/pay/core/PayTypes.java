package org.magneton.module.pay.core;

import java.util.Locale;

import javax.annotation.Nullable;

import org.magneton.module.pay.wechat.v3.core.WxPayTradeType;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public enum PayTypes {

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
