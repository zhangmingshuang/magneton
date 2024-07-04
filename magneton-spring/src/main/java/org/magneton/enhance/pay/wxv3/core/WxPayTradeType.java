package org.magneton.enhance.pay.wxv3.core;

import com.google.common.base.Objects;
import org.magneton.enhance.pay.core.TradeType;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public enum WxPayTradeType implements TradeType {

	/**
	 * 公众号支付
	 */
	JSAPI(0, "公众号支付"),

	/**
	 * 扫码支付
	 */
	NATIVE(1, "扫码支付"),

	/**
	 * APP支付
	 */
	APP(2, "APP支付"),

	/**
	 * 付款码支付
	 */
	MICROPAY(3, "付款码支付"),

	/**
	 * H5支付
	 */
	MWEB(4, "H5支付"),

	/**
	 * 刷脸支付
	 */
	FACEPAY(5, "刷脸支付");

	private final int code;

	private final String description;

	WxPayTradeType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public boolean isName(String name) {
		return Objects.equal(name, this.name());
	}

}