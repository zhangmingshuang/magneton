package org.magneton.module.pay.wechat.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
public class WechatPayPreOrderRes {

	/**
	 * 预支付交易会话标识
	 */
	@JsonProperty("prepay_id")
	private String prepayId;

}
