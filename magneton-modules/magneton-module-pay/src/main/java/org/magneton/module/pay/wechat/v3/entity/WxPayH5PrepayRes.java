package org.magneton.module.pay.wechat.v3.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class WxPayH5PrepayRes {

	/**
	 * 支付跳转链接 h5_url string[1,512] 是
	 * h5_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付，h5_url的有效期为5分钟。
	 * 示例值：https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx2016121516420242444321ca0631331346&package=1405458241
	 */
	@JsonProperty("h5_url")
	private String h5Url;

}
