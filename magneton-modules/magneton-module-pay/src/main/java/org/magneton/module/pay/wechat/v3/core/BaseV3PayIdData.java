package org.magneton.module.pay.wechat.v3.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础支付ID数据
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class BaseV3PayIdData {

	/**
	 * 应用ID string[1,32]
	 *
	 * 由微信生成的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，直连模式下该id应为APP应用的id。
	 *
	 * 示例值：wxd678efh567hg6787
	 */
	private String appid;

	/**
	 * 直连商户号 string[1,32]
	 *
	 * 直连商户的商户号，由微信支付生成并下发。
	 *
	 * 示例值：1230000109
	 */
	private String mchid;

	protected void setAppid(String appid) {
		this.appid = appid;
	}

	protected String getAppid() {
		return this.appid;
	}

	protected void setMchid(String mchid) {
		this.mchid = mchid;
	}

	protected String getMchid() {
		return this.mchid;
	}

}
