package org.magneton.module.pay.wechat.v3.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WxPayConfig {

	/**
	 * 服务商在开放平台申请的应用appid。 示例值：wx8888888888888888
	 */
	private AppId appId;

	/**
	 * 服务商户号，由微信支付生成并下发
	 */
	private String merchantId;

	/**
	 * 商户API证书的证书序列号
	 */
	private String merchantSerialNumber;

	/**
	 * 商户API私钥文件
	 *
	 * 如："/path/to/apiclient_key.pem"
	 */
	private String merchantPrivateKeyFile;

	/**
	 * apiV3Key：V3密钥
	 */
	private String apiV3Key;

	/**
	 * 异步通知地址
	 */
	private String notifyUrl;

	@Setter
	@Getter
	@ToString
	public static class AppId {

		private String app;

		private String jsapi;

		private String h5;

		/**
		 * 分账时的APPID，如果没有配置，则默认使用 {@link #app}
		 */
		private String profitSharing;

	}

}
